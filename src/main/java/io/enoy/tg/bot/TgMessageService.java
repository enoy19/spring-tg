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

@Service
@Scope("tg")
@RequiredArgsConstructor
public class TgMessageService {

	private final TgBot tgBot;

	public Message sendMessage(String message) {

		TgContext context = TgContextHolder.currentContext();
		try {
			return tgBot.execute(new SendMessage(context.getUserId(), message));
		} catch (TelegramApiException e) {
			throw new IllegalStateException(e);
		}

	}

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
