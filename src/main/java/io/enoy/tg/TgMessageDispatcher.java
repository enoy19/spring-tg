package io.enoy.tg;

import io.enoy.tg.action.TgController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Scope("tg")
@RequiredArgsConstructor
public class TgMessageDispatcher {

	private final ApplicationContext context;
	private boolean initialized = false;
	private List<Object> controllers = new ArrayList<>();

	private Message currentCommand;
	private List<Message> currentArguments = new ArrayList<>();

	public void dispatch(Message message) throws TgDispatchException {
		if (!initialized) {
			initialize();
			initialized = true;
		}

		if(Objects.isNull(currentCommand)) {
			this.currentCommand = message;
			setupCommand();
		} else {
			currentArguments.add(message);
			processArguments();
		}

	}

	private void setupCommand() {

	}

	private void processArguments() {
	}

	public static class TgDispatchException extends Exception {
		public TgDispatchException(Throwable cause) {
			super(cause);
		}
	}

}
