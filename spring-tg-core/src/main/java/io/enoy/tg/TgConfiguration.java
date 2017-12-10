package io.enoy.tg;

import io.enoy.tg.bot.TgBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

/**
 * Spring configuration that loads all relevant spring-tg components
 * @author Enis Ö.
 * @see EnableTgBot
 */
@Configuration
@ComponentScan(value = "io.enoy.tg")
@RequiredArgsConstructor
public class TgConfiguration {

	private final ApplicationContext context;

	/**
	 * initializes the central telegram bot using the wonderful {@link ApiContextInitializer}.
	 * @return TgBot bean
	 */
	@Bean
	public TgBot tgBot() {
		ApiContextInitializer.init();
		return new TgBot(context);
	}

	@Bean
	public TelegramBotsApi telegramBotsApi() {
		return new TelegramBotsApi();
	}

}
