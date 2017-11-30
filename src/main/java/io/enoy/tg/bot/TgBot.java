package io.enoy.tg.bot;

import io.enoy.tg.bot.TgMessageDispatcher.TgDispatchException;
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
				TgContextHolder.setupContext(message.getFrom());
				TgContext tgContext = TgContextHolder.currentContext();

				synchronized (tgContext) {
					TgMessageDispatcher dispatcher = context.getBean(TgMessageDispatcher.class);
					try {
						dispatcher.dispatch(message);
					} catch (TgDispatchException e) {
						sendPrefixed(tgContext.getUserId(), "ERROR", e.getMessage());
						dispatcher.clear();
						log.error(e.getMessage(), e);
					} catch (Exception e) {
						sendPrefixed(tgContext.getUserId(), "FATAL ERROR", "Something went wrong :(");
						dispatcher.clear();
						log.error(e.getMessage(), e);
					}
				}

			});
			processThread.setDaemon(true);
			processThread.start();
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

	private void sendPrefixed(long userId, String prefix, String message) {
		send(userId, String.format("%s:%n%s", prefix, message));
	}

	private void send(long userId, String message) {
		try {
			execute(new SendMessage(userId, message));
		} catch (TelegramApiException e1) {
			throw new IllegalStateException(e1);
		}
	}

}
