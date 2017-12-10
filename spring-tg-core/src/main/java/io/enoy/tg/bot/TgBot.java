package io.enoy.tg.bot;

import io.enoy.tg.bot.TgMessageDispatcher.TgDispatchException;
import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import io.enoy.tg.security.TgAuthentication;
import io.enoy.tg.security.TgGrantedAuthoritiesProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link TelegramLongPollingBot} that takes care of creating request Threads, setting up the {@link TgContext} as well as forwarding the message to the {@link TgMessageDispatcher}
 *
 * @author Enis Ö.
 * @see TgContext
 * @see TgMessageDispatcher
 */
@RequiredArgsConstructor
@Slf4j
public class TgBot extends TelegramLongPollingBot {

	private final ApplicationContext context;
	private final Optional<TgGrantedAuthoritiesProvider> notRequiredGrantedAuthoritiesProvider;

	@Value("${bot.token}")
	private String botToken;

	@Value("${bot.name}")
	private String botUsername;

	private TgGrantedAuthoritiesProvider grantedAuthoritiesProvider;

	@PostConstruct
	private void init() {
		if (notRequiredGrantedAuthoritiesProvider.isPresent()) {
			this.grantedAuthoritiesProvider = notRequiredGrantedAuthoritiesProvider.get();
		}
	}

	@Override
	public synchronized void onUpdateReceived(final Update update) {
		if (update.hasMessage()) {
			final Message message = update.getMessage();

			Thread processThread = new Thread(() -> {
				TgContextHolder.setupContext(message);
				TgContext tgContext = TgContextHolder.currentContext();

				if (Objects.nonNull(grantedAuthoritiesProvider))
					setupSecurityContext(tgContext);

				synchronized (tgContext) {
					TgMessageDispatcher dispatcher = context.getBean(TgMessageDispatcher.class);
					Throwable exception = null;

					try {
						dispatcher.dispatch(message);
					} catch (TgDispatchException e) {
						exception = e;
						sendPrefixed(tgContext.getChatId(), "ERROR", e.getMessage());
					} catch (AccessDeniedException e) {
						exception = e;
						sendPrefixed(tgContext.getChatId(), "ACCESS DENIED", "Insufficient access rights.");
					} catch (Exception e) {
						exception = e;
						sendPrefixed(tgContext.getChatId(), "FATAL ERROR", "Something went wrong :(");
					} finally {
						if (Objects.nonNull(exception)) {
							dispatcher.clear();
							log.error(exception.getMessage(), exception);
						}
					}
				}

			});
			processThread.setName("TgRequest");
			processThread.setDaemon(true);
			processThread.start();
		}
	}

	private void setupSecurityContext(TgContext tgContext) {
		Collection<GrantedAuthority> tgGrantedAuthorities = grantedAuthoritiesProvider.getAuthoritiesOf(tgContext);
		TgAuthentication tgAuthentication = new TgAuthentication(tgContext, tgGrantedAuthorities);
		SecurityContextHolder.getContext().setAuthentication(tgAuthentication);
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	private void sendPrefixed(long userId, String prefix, String message) {
		send(userId, String.format("%s:%n%s", prefix, message));
	}

	private void send(long userId, String message) {
		try {
			execute(new SendMessage(userId, message));
		} catch (TelegramApiException e1) {
			throw new IllegalStateException(e1);
		}
	}

}
