package io.enoy.tg.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class TgBotService {

	private final TgBot tgBot;
	private final TelegramBotsApi telegramBotsApi;

	@PostConstruct
	private void init() {
		try {
			telegramBotsApi.registerBot(tgBot);
		} catch (TelegramApiRequestException e) {
			log.error(String.format("Failed to initialize Telegram Bot: %s", e.getMessage()), e);
		}
	}

}