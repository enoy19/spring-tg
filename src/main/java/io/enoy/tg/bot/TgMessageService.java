package io.enoy.tg.bot;

import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Telegram message service. Used to send simple text messages to a user.
 * @author Enis Ö.
 */
@Service
@Scope("tg")
@RequiredArgsConstructor
public class TgMessageService {
	// TODO: create some non-tg-scoped message service that always requires a user id.
	// TODO: implements every message type that can be sent in tg

	private final TgBot tgBot;

	/**
	 * Sends a simple text message to the {@link org.telegram.telegrambots.api.objects.User} in the current {@link TgContext}
	 * @param message
	 * @return Message that was sent to the user
	 */
	public Message sendMessage(String message) {

		TgContext context = TgContextHolder.currentContext();
		try {
			return tgBot.execute(new SendMessage(context.getUserId(), message));
		} catch (TelegramApiException e) {
			throw new IllegalStateException(e);
		}

	}

	/**
	 * Edits the {@link Message} instance to the given text
	 * @param messageText
	 * @param message
	 */
	public void editMessage(String messageText, Message message) {

		TgContext context = TgContextHolder.currentContext();

		EditMessageText editMessage = new EditMessageText();
		editMessage.setChatId(context.getUserId());
		editMessage.setMessageId(message.getMessageId());
		editMessage.setText(messageText);

		try {
			tgBot.execute(editMessage);
		} catch (TelegramApiException e) {
			throw new IllegalStateException(e);
		}
	}


}
