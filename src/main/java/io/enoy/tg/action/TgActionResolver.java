package io.enoy.tg.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Scope("tg")
@RequiredArgsConstructor
@Slf4j
public final class TgActionResolver {

	private final ApplicationContext context;
	private List<TgAction> actions = new ArrayList<>();

	@PostConstruct
	private void init() {
		Set<BeanDefinition> tgControllerDefinition = seekControllers();
		List<Object> controllers = addControllers(tgControllerDefinition);
		resolveActions(controllers);
	}

	private void resolveActions(List<Object> controllers) {
		for (Object controller : controllers) {
			log.info("Resolving TgController: {}", controller.getClass().getName());
			resolveAction(controller);
			log.info("TgController Resolved: {}", controller.getClass().getName());
		}
	}

	private void resolveAction(Object controller) {
		List<Method> thMethods = getTgRequestMethods(controller);
		thMethods.forEach(m -> checkMethod(controller.getClass(), m));

		// i admit this looks stupid (get(0))... but its 2 am
		int maxParameterCount = thMethods.stream().mapToInt(Method::getParameterCount).max().getAsInt();
		TgActionRequestHandler action = resolveTgActionRequestHandler(thMethods, 1, maxParameterCount, null).get(0);

	}

	private List<TgActionRequestHandler> resolveTgActionRequestHandler(List<Method> thMethods, int parameterCount, int maxParameterDepth, final TgActionRequestHandler parent) {
		List<Method> paramCountFittingMethods =
				thMethods.stream()
						.filter(m -> m.getParameterCount() == parameterCount)
						.collect(Collectors.toList());

		if (parent == null && paramCountFittingMethods.size() > 1)
			throw new TgActionResolveException("Illegal root method count. You can only have one request with one parameter.");

		List<TgActionRequestHandler> handlers =
				paramCountFittingMethods.stream()
						.map(m -> {
							TgActionRequestHandler requestHandler = new TgActionRequestHandler();

							requestHandler.setMethod(m);
							requestHandler.setParent(parent);

							Class<?> parameterType = m.getParameterTypes()[parameterCount - 1];
							requestHandler.setParamType(parameterType);

							return requestHandler;
						}).collect(Collectors.toList());

		if (Objects.nonNull(parent))
			parent.setChildren(handlers);

		if (!handlers.isEmpty())
			handlers.forEach(h -> resolveTgActionRequestHandler(thMethods, parameterCount + 1, maxParameterDepth, h));
		else {
			if(parameterCount < maxParameterDepth) {
				// ERROR! you need to specify every possibility TODO: proof of concept
			}
		}

		return handlers;
	}

	private List<Method> getTgRequestMethods(Object controller) {
		List<Method> methods = new ArrayList<>();

		Class<?> controllerClass = controller.getClass();
		for (Method method : controllerClass.getMethods())
			if (method.isAnnotationPresent(TgRequest.class))
				methods.add(method);

		return methods;
	}

	private void checkMethod(Class<?> controllerClass, Method method) {
		if (!method.isAccessible())
			throw new TgActionResolveException("TgRequest must be public. TgController: " + controllerClass.getName());
	}

	private List<Object> addControllers(Set<BeanDefinition> candidateComponents) {
		List<Object> controllers = new ArrayList<>();

		for (BeanDefinition beanDefinition : candidateComponents) {
			try {
				Object controller = context.getBean(Class.forName(beanDefinition.getBeanClassName()));
				controllers.add(controller);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}

		return controllers;
	}

	private Set<BeanDefinition> seekControllers() {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(TgController.class));
		return scanner.findCandidateComponents("");
	}

	public static class TgActionResolveException extends RuntimeException {

		public TgActionResolveException(String message) {
			super(message);
		}

	}

}
