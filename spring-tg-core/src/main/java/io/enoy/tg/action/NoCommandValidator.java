package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;

/**
 * A useless {@link CommandValidator} that is just ignored by the {@link io.enoy.tg.bot.TgMessageDispatcher}
 * when specified in {@link TgController} (which it is by default)
 *
 * @author Enis Ö.
 * @see CommandValidator
 * @see TgController
 * @see TgController#commandValidator()
 * @see io.enoy.tg.bot.TgMessageDispatcher
 */
public final class NoCommandValidator implements CommandValidator {

	private NoCommandValidator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean validate(Message message) {
		throw new UnsupportedOperationException();
	}

}
