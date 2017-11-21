package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;

public final class NoCommandValidator implements CommandValidator {

	private NoCommandValidator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean accepts(Message message) {
		throw new UnsupportedOperationException();
	}

}
