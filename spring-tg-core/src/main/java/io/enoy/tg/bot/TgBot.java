package io.enoy.tg.bot;

import io.enoy.tg.bot.exceptions.TgExceptionDispatcher;
import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * A {@link TelegramLongPollingBot} that takes care of creating request Threads, setting up the {@link TgContext} as well as forwarding the message to the {@link TgMessageDispatcher}
 *
 * @author Enis Ö.
 * @see TgContext
 * @see TgMessageDispatcher
 */
@RequiredArgsConstructor
@Slf4j
public class TgBot extends TelegramLongPollingBot {

	private final ApplicationContext context;

	@Value("${bot.token}")
	private String botToken;

	@Value("${bot.name}")
	private String botUsername;

	@Override
	public synchronized void onUpdateReceived(final Update update) {
		if (update.hasMessage()) {
			final Message message = update.getMessage();

			Thread processThread = new Thread(() -> {
				handleMessage(message);
			});
			processThread.setName("TgRequest");
			processThread.setDaemon(true);
			processThread.start();
		}
	}

	private void handleMessage(Message message) {
		TgContextHolder.setupContext(message);
		TgContext tgContext = TgContextHolder.currentContext();

		synchronized (tgContext) {
			dispatchMessage(message, tgContext);
		}
	}

	public void dispatchMessage(Message message, TgContext tgContext) {
		TgMessageDispatcher messageDispatcher = context.getBean(TgMessageDispatcher.class);

		try {
			messageDispatcher.dispatch(message);
		} catch (Exception e) {
			messageDispatcher.clear();
			handleException(tgContext, e);
		}
	}

	private void handleException(TgContext tgContext, Exception e) {
		TgExceptionDispatcher exceptionDispatcher = context.getBean(TgExceptionDispatcher.class);
		boolean sucessfullyDispatched = exceptionDispatcher.dispatchException(e);

		if (!sucessfullyDispatched) {
			log.error("Unhandled exception");
			log.error(e.getMessage(), e);
			send(tgContext.getChatId(), "Fatal Error\nSomething went wrong :(");
		}
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	private void send(long userId, String message) {
		try {
			execute(new SendMessage(userId, message));
		} catch (TelegramApiException e1) {
			throw new IllegalStateException(e1);
		}
	}

}
