package io.enoy.tg;

import io.enoy.tg.bot.TgBot;
import io.enoy.tg.security.TgGrantedAuthoritiesProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

import java.util.Optional;

/**
 * Spring configuration that loads all relevant spring-tg components
 * @author Enis Ö.
 * @see EnableTgBot
 */
@Configuration
@ComponentScan(value = "io.enoy.tg")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class TgConfiguration {

	private final ApplicationContext context;
	private final Optional<TgGrantedAuthoritiesProvider> notRequiredGrantedAuthoritiesProvider;

	/**
	 * initializes the central telegram bot using the wonderful {@link ApiContextInitializer}.
	 * @return TgBot bean
	 */
	@Bean
	public TgBot tradeBot() {
		ApiContextInitializer.init();
		return new TgBot(context, notRequiredGrantedAuthoritiesProvider);
	}

	@Bean
	public TelegramBotsApi telegramBotsApi() {
		return new TelegramBotsApi();
	}

}
