package io.enoy.tg.scope.context;

import io.enoy.tg.action.TgActionContext;
import org.telegram.telegrambots.api.objects.User;

public class TgContext {

	private User user;
	private long userId;
	private TgActionContext currentActionContext;

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

	public TgActionContext getCurrentActionContext() {
		return currentActionContext;
	}

	public void setCurrentActionContext(TgActionContext currentActionContext) {
		this.currentActionContext = currentActionContext;
	}

	public void resetActionContext() {
		setCurrentActionContext(null);
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
