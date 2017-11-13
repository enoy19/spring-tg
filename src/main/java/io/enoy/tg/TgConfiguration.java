package io.enoy.tg;

import io.enoy.tg.bot.TgBot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

@Configuration
@ComponentScan("io.enoy.tg")
@RequiredArgsConstructor
public class TgConfiguration {

	private final ApplicationContext context;

	@Bean
	public TgBot tradeBot() {
		ApiContextInitializer.init();
		return new TgBot(context);
	}

	@Bean
	public TelegramBotsApi telegramBotsApi() {
		return new TelegramBotsApi();
	}

}
