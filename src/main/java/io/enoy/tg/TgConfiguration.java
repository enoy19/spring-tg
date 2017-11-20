package io.enoy.tg;

import io.enoy.tg.bot.TgBot;
import io.enoy.tg.request.TgControllerResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan("io.enoy.tg")
@RequiredArgsConstructor
public class TgConfiguration {

	private final ApplicationContext context;
	private final TgControllerResolver resolver;

	@PostConstruct
	private void init() {
		resolver.resolveTgController();
	}

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
