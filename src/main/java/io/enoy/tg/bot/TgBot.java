package io.enoy.tg.bot;

import io.enoy.tg.TgMessageDispatcher;
import io.enoy.tg.TgMessageDispatcher.TgDispatchException;
import io.enoy.tg.action.request.TgRequestResult;
import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@RequiredArgsConstructor
public class TgBot extends TelegramLongPollingBot {

	private final ApplicationContext context;

	@Value("${bot.token}")
	private String botToken;

	@Value("${bot.name}")
	private String botUsername;

	@Override
	public void onUpdateReceived(final Update update) {
		if (update.hasMessage()) {
			Thread processThread = new Thread(() -> processUpdate(update.getMessage()));
			processThread.setDaemon(true);
			processThread.start();
		}
	}

	private void processUpdate(Message message) {
		TgContextHolder.setupContext(message.getFrom());
		TgContext tgContext = TgContextHolder.currentContext();

		synchronized (tgContext) {
			TgMessageDispatcher dispatcher = context.getBean(TgMessageDispatcher.class);
			try {
				dispatcher.dispatch(message);
			} catch (TgDispatchException e) {
				sendDispatchError(tgContext.getUserId(), e);
			}
		}

	}

	private void sendDispatchError(long userId, TgDispatchException e) {
		try {
			execute(new SendMessage(userId, String.format("ERROR:%n%s", e.getMessage())));
		} catch (TelegramApiException e1) {
			throw new IllegalStateException(e1);
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

}
