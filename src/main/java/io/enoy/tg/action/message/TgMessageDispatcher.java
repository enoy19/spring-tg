package io.enoy.tg.action.message;

import io.enoy.tg.action.TgAction;
import io.enoy.tg.action.TgActionDefinition;
import io.enoy.tg.action.TgActionProcessor;
import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

	private final Set<TgActionDefinition> definitions;
	private final Set<TgActionProcessor<?>> processors;

	public void dispatch(Message message) throws DispatchException {
		TgContext tgContext = TgContextHolder.currentContext();
		TgAction tgAction = tgContext.getCurrentAction();

		if (Objects.isNull(tgAction)) {
			tgAction = startAction(message);
			tgContext.setCurrentAction(tgAction);
		}

		tgAction.getInputs().add(message);
		TgActionProcessor<?> processor = getTgActionProcessor(tgAction);
		boolean done = processor.process(tgAction);

		if (done)
			tgContext.resetAction();
	}

	private TgAction startAction(Message message) throws DispatchException {
		Optional<TgActionDefinition> optDefinition =
				definitions.stream()
						.filter(tad -> tad.accepts(message))
						.findFirst();

		if (!optDefinition.isPresent())
			throw new DispatchException("Command does not exist");

		TgActionDefinition definition = optDefinition.get();
		Class<? extends TgActionDefinition> definitionClass = definition.getClass();

		return new TgAction(definitionClass);
	}

	private TgActionProcessor<?> getTgActionProcessor(TgAction tgAction) throws DispatchException {
		Optional<TgActionProcessor<?>> optProcessor = processors.stream()
				.filter(p -> p.getDefinitionClass().equals(tgAction.getDefinitionClass()))
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
