package io.enoy.tg.scope.context;

import org.telegram.telegrambots.api.objects.User;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TgContext tgContext = (TgContext) o;

		return userId == tgContext.userId;
	}

	@Override
	public int hashCode() {
		return (int) (userId ^ (userId >>> 32));
	}
}
