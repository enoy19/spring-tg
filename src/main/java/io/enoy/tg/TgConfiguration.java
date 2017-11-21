package io.enoy.tg;

import io.enoy.tg.bot.TgBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

@Configuration
@ComponentScan(value = "io.enoy.tg",
		excludeFilters = @Filter(type = FilterType.REGEX, pattern = "io\\.enoy\\.tg\\.example\\.*"))
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
