package io.enoy.tg.request;

import io.enoy.tg.action.SimpleTgActionDefinition;
import io.enoy.tg.action.TgActionContext;
import io.enoy.tg.action.TgActionDefinition;
import io.enoy.tg.action.TgActionRegexDefinition;
import org.telegram.telegrambots.api.objects.Message;

final class NoTgActionRegexDefinition implements TgActionRegexDefinition<TgActionContext> {

	NoTgActionRegexDefinition() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getRegex() {
		return null;
	}

	@Override
	public Class<TgActionContext> getActionContextClass() {
		return null;
	}

	@Override
	public TgActionContext createContext(Message message) {
		return null;
	}
}