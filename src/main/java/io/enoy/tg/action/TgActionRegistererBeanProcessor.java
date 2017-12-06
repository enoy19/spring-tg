package io.enoy.tg.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class TgActionRegistererBeanProcessor implements BeanPostProcessor {

	private static final String TG_ACTION_BEAN_SUFFIX = "$tgAction";
	private final TgActionRegisterer tgActionRegisterer;
	private final ConfigurableBeanFactory beanFactory;

	@PostConstruct
	private void init() {
		log.info("[REGISTERING] TgActions...");
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

		if (bean.getClass().isAnnotationPresent(TgController.class)) {
			registerAction(bean);
		}

		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}


	private void registerAction(Object controller) {
		String controllerClassName = controller.getClass().getName();
		log.info(" - [REGISTERING] TgController: {}", controllerClassName);

		TgAction tgAction = tgActionRegisterer.registerAction(controller);
		String beanName = controllerClassName + TG_ACTION_BEAN_SUFFIX;
		beanFactory.registerSingleton(beanName, tgAction);

		log.info(" - [REGISTERING DONE] TgController: {} ({})", controllerClassName, beanName);
	}

}
