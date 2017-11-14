package io.enoy.tg.action.message;

import io.enoy.tg.action.TgActionContext;
import io.enoy.tg.action.TgActionDefinition;
import io.enoy.tg.action.TgActionProcessor;
import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Scope("tg")
@RequiredArgsConstructor
public class TgMessageDispatcher {

    private final ApplicationContext context;
    private final Set<TgActionDefinition<?>> definitions;
    private final Set<TgActionProcessor<?, ?>> processors;

    public void dispatch(Message message) throws DispatchException {
        TgContext tgContext = TgContextHolder.currentContext();
        TgActionContext tgActionContext = tgContext.getCurrentActionContext();

        if (Objects.isNull(tgActionContext)) {
            tgActionContext = startAction(message, getTgActionDefinition(message));
            tgContext.setCurrentActionContext(tgActionContext);
        }

        tgActionContext.getInputs().add(message);

        TgActionDefinition<?> definition = context.getBean(tgActionContext.getDefinitionClass());

        @SuppressWarnings("unchecked")
        TgActionProcessor<TgActionContext, ?> processor =
                (TgActionProcessor<TgActionContext, ?>) getTgActionProcessor(definition);

        boolean done = processor.process(tgActionContext);

        if (done)
            tgContext.resetActionContext();
    }

    @SuppressWarnings("unchecked")
    private TgActionContext startAction(Message message, TgActionDefinition<?> definition) throws DispatchException {
        TgActionContext action = definition.createAction(message);
        action.setDefinitionClass((Class<? extends TgActionDefinition<?>>) definition.getClass());
        return action;
    }

    private TgActionDefinition getTgActionDefinition(Message message) throws DispatchException {
        Optional<TgActionDefinition<?>> optDefinition =
                definitions.stream()
                        .filter(tad -> tad.accepts(message))
                        .findFirst();

        if (!optDefinition.isPresent())
            throw new DispatchException("Command does not exist");

        return optDefinition.get();
    }

    private TgActionProcessor<?, ?> getTgActionProcessor(TgActionDefinition<?> actionDefinition) throws DispatchException {
        final Class<?> actionDefinitionClass = actionDefinition.getClass();
        final Class<?> actionDefinitionContextClass = actionDefinition.getActionContextClass();

        Optional<TgActionProcessor<?, ?>> optProcessor = processors.stream()
                .filter(p -> p.getActionDefinitionClass().equals(actionDefinitionClass))
                .filter(p -> p.getActionContextClass().equals(actionDefinitionContextClass))
                .findFirst();

        if (!optProcessor.isPresent())
            throw new DispatchException("Processor not found");

        return optProcessor.get();
    }

    public static class DispatchException extends Exception {

        DispatchException(String message) {
            super(message);
        }

    }

}
