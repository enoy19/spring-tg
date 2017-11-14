package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;

public interface TgActionDefinition<T extends TgActionContext> {

	String getName();
	String getDescription();
	boolean accepts(Message message);
	T createAction(Message message);
	Class<T> getActionContextClass();

}
