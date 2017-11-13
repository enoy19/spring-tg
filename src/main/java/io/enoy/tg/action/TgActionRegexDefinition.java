package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;

public interface TgActionRegexDefinition extends TgActionDefinition {

	@Override
	default boolean accepts(Message message) {
		String text = message.getText();
		text = text.trim();
		return text.matches(getRegex());
	}

	String getRegex();

}
