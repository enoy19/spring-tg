package io.enoy.tg.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class TgBotService {

	private final TgBot tgBot;
	private final TelegramBotsApi telegramBotsApi;

	@PostConstruct
	private void init() {
		try {
			telegramBotsApi.registerBot(tgBot);
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}