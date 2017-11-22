package io.enoy.tg.scope.context;

import org.telegram.telegrambots.api.objects.User;

import java.util.HashMap;
import java.util.Map;

/**
 * The Telegram context holder. It keeps {@link ThreadLocal} references of all {@link TgContext}s
 * @author Enis Ö.
 */
public class TgContextHolder {

	private static final Map<Integer, TgContext> tgUserContexts = new HashMap<>();
	private static final ThreadLocal<TgContext> tgContextThreadLocal = new ThreadLocal<>();

	public static TgContext currentContext() {
		return tgContextThreadLocal.get();
	}

	public static void setupContext(User user) {
		Integer userId = user.getId();

		if (!tgUserContexts.containsKey(userId)) {
			TgContext value = new TgContext();
			value.setUser(user);
			tgUserContexts.put(userId, value);
		}

		TgContext tgContext = tgUserContexts.get(userId);

		tgContextThreadLocal.set(tgContext);
	}

	public static TgContext getContextOfUserId(Integer userId) {
		return tgUserContexts.get(userId);
	}

}
