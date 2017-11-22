package io.enoy.tg.scope.context;

import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.api.objects.User;

/**
 * The Telegram context. It holds {@link User} information.
 * @author Enis Ö.
 * @see TgContextHolder
 * @see User
 */
@EqualsAndHashCode(of = "userId")
public class TgContext {

	private User user;
	private long userId;

	public void setUser(User user) {
		this.user = user;
		this.userId = user.getId();
	}

	public User getUser() {
		return user;
	}

	public long getUserId() {
		return userId;
	}

}
