package io.enoy.tg.bot;

import io.enoy.tg.bot.exceptions.TgExceptionDispatcher;
import io.enoy.tg.scope.context.TgContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;

import java.util.Objects;

/**
 * handles messages that were received in {@link TgBot}.
 * also handles any exception using {@link io.enoy.tg.bot.exceptions.TgExceptionHandler}
 * @author Enis Ö.
 * @see TgBot
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TgBotMessageHandler {

	private final ApplicationContext context;

	public void handleMessage(Message message, TgContext tgContext) throws TgBotMessageHandleException {
		TgMessageDispatcher messageDispatcher = context.getBean(TgMessageDispatcher.class);

		try {
			messageDispatcher.dispatch(message);
		} catch (Exception e) {
			messageDispatcher.clear();
			handleException(tgContext, e);
		}

	}

	private void handleException(TgContext tgContext, Exception e) throws TgBotMessageHandleException {
		TgExceptionDispatcher exceptionDispatcher = context.getBean(TgExceptionDispatcher.class);

		Throwable rootCause = getRootCause(e);
		boolean sucessfullyDispatched = exceptionDispatcher.dispatchException(rootCause);

		if (!sucessfullyDispatched) {
			log.error("Unhandled exception");
			log.error(e.getMessage(), e);
			throw new TgBotMessageHandleException("Fatal Error\nSomething went wrong :(");
		}
	}

	private Throwable getRootCause(Throwable throwable) {
		if (Objects.nonNull(throwable.getCause()))
			return getRootCause(throwable.getCause());

		return throwable;
	}

	static class TgBotMessageHandleException extends Exception {
		TgBotMessageHandleException(String message) {
			super(message);
		}
	}

}
