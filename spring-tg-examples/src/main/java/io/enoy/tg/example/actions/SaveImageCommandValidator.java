package io.enoy.tg.example.actions;

import io.enoy.tg.action.CommandValidator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;

@Component
public class SaveImageCommandValidator implements CommandValidator {

	@Override
	public boolean validate(Message message) {
		return message.hasPhoto();
	}

}
