package io.enoy.tg.example.actions;

import io.enoy.tg.action.CommandValidator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class FooBarCommandValidator implements CommandValidator {

	private final AtomicBoolean state = new AtomicBoolean();

	@Override
	public boolean validate(Message message) {
		return message.hasText()
				&& message.getText().trim().equalsIgnoreCase("/foo")
				&& state.getAndSet(!state.get());
	}

}
