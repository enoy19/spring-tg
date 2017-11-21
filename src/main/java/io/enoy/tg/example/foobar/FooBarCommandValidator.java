package io.enoy.tg.example.foobar;

import io.enoy.tg.action.CommandValidator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class FooBarCommandValidator implements CommandValidator {

	private AtomicBoolean state = new AtomicBoolean();

	@Override
	public boolean validate(Message message) {
		return state.getAndSet(!state.get());
	}

}
