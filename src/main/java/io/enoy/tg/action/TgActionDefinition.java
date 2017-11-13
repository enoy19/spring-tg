package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;

public interface TgActionDefinition {

	String getName();
	String getDescription();
	boolean accepts(Message message);

}
