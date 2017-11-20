package io.enoy.tg.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class TgControllerResolver {

	private final ListableBeanFactory listableBeanFactory;

	public void resolveTgController() {
		Map<String, Object> controllers = listableBeanFactory.getBeansWithAnnotation(Controller.class);
		Object findTgMethods = findTgMethods(controllers.values());
	}

	private Object findTgMethods(Collection<Object> values) {

		values.stream()
				.map(this::getTgMethod)

	}

	private void getTgMethod(Object o) {

		Class<?> clazz = o.getClass();
		clazz.getMetho

	}

}
