package io.enoy.tg.action.message;

import io.enoy.tg.bot.TgBot;
import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Service
@Scope("tg")
@RequiredArgsConstructor
public class TgMessageService {

	private final TgBot tgBot;

	public void sendMessage(String message) {

		TgContext context = TgContextHolder.currentContext();
		try {
			tgBot.execute(new SendMessage(context.getUserId(), message));
		} catch (TelegramApiException e) {
			throw new IllegalStateException(e);
		}

	}

}
