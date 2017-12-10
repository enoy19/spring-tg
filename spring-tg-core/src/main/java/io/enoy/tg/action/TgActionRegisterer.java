package io.enoy.tg.action;

import io.enoy.tg.action.request.TgParameterType;
import io.enoy.tg.action.request.TgRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Resolves and registers any {@link TgController} annotated bean in the application context
 *
 * @author Enis Ö.
 * @see TgAction
 */
@Service
@RequiredArgsConstructor
@Slf4j
public final class TgActionRegisterer {

	public TgAction registerAction(Object controller) {
		List<Method> thMethods = getTgRequestMethods(controller);

        TgController tgController = getTgController(controller);
        List<TgActionRequestHandler> requestHandlers = createTgActionRequestHandlers(thMethods);
        TgActionRequestHandler tgPreActionHandler = createTgPreActionHandler(controller);
        TgActionRequestHandler tgPostActionHandler = createTgPostActionHandler(controller);

        return new TgAction(
                controller.getClass(),
                tgController.name(),
                tgController.description(),
                tgController.regex(),
                tgController.commandValidator(),
                requestHandlers,
                tgPreActionHandler,
                tgPostActionHandler
        );

    }

    private TgActionRequestHandler createTgPreActionHandler(Object controller) {
        Method preAction = getTgPreActionMethod(controller);

        if (Objects.nonNull(preAction)) {
            checkTgPrePostActionMethod(preAction);
            return createTgActionRequestHandler(preAction);
        }

        return null;
    }

    private TgActionRequestHandler createTgPostActionHandler(Object controller) {
        Method postAction = getTgPostActionMethod(controller);

        if (Objects.nonNull(postAction)) {
            checkTgPrePostActionMethod(postAction);
            return createTgActionRequestHandler(postAction);
        }

        return null;
    }

    private void checkTgPrePostActionMethod(Method preOrPostActionMethod) {

        if (preOrPostActionMethod.getParameterCount() != 0)
            throw new IllegalStateException(String.format("Pre and Post method parameters must be empty. Method: %s.%s",
                    preOrPostActionMethod.getDeclaringClass().getName(),
                    preOrPostActionMethod.getName())
            );

    }

    private TgController getTgController(Object controller) {
        return controller.getClass().getAnnotation(TgController.class);
    }

    private List<TgActionRequestHandler> createTgActionRequestHandlers(List<Method> thMethods) {
        return thMethods.stream()
                .map(this::createTgActionRequestHandler)
                .collect(Collectors.toList());
    }

    private TgActionRequestHandler createTgActionRequestHandler(Method method) {
        String name = getTgRequestName(method);
        TgParameterType[] parameters = getTgParameterTypes(method);

        return new TgActionRequestHandler(name, method, parameters);
    }

    private TgParameterType[] getTgParameterTypes(Method method) {
        Class<?>[] types = method.getParameterTypes();
        TgParameterType[] parameters = new TgParameterType[types.length];

        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];

            TgParameterType tgParameterType = TgParameterType.getMatchingParameterType(type);
            parameters[i] = tgParameterType;
        }

        return parameters;
    }

    private String getTgRequestName(Method method) {
        TgRequest requestAnnotation = method.getAnnotation(TgRequest.class);
        String name;

        if (Objects.isNull(requestAnnotation) || requestAnnotation.value().trim().isEmpty())
            name = method.getName();
        else
            name = requestAnnotation.value();
        return name;
    }

    private List<Method> getTgRequestMethods(Object controller) {
        return getTgMethods(controller, TgRequest.class);
    }

    private Method getTgPreActionMethod(Object controller) {
        List<Method> tgMethods = getTgMethods(controller, TgPreAction.class);

        if (tgMethods.isEmpty())
            return null;

        checkTgPrePostMethodList(controller, tgMethods, "TgPreAction");

        return tgMethods.get(0);
    }

    private Method getTgPostActionMethod(Object controller) {
        List<Method> tgMethods = getTgMethods(controller, TgPostAction.class);

        if (tgMethods.isEmpty())
            return null;

        checkTgPrePostMethodList(controller, tgMethods, "TgPostAction");

        return tgMethods.get(0);
    }

    private void checkTgPrePostMethodList(Object controller, List<Method> tgMethods, String type) {
        if (tgMethods.size() != 1)
            throw new IllegalStateException(
                    String.format("Only one %s allowed. Found: %d. Controller class: %s",
                            type,
                            tgMethods.size(),
                            controller.getClass().getName())
            );
    }

    private List<Method> getTgMethods(Object controller, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();

        Class<?> controllerClass = controller.getClass();
	    for (Method method : controllerClass.getDeclaredMethods())
		    if (method.isAnnotationPresent(annotation))
                methods.add(method);

        return methods;
    }

}
