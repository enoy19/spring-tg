package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;

public interface TgActionDefinition<T extends TgActionContext> extends TgActionContextFactory<T> {

	String getName();
	String getDescription();
	boolean accepts(Message message);
	Class<T> getActionContextClass();

}
