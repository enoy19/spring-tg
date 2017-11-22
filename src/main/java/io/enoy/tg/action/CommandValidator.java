package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;

/**
 * used to implement a logic for resolving if an {@link TgController} is valid for a given command.
 * @author Enis Ö.
 * @see TgController
 * @see TgController#regex()
 * @see NoCommandValidator
 */
@FunctionalInterface
public interface CommandValidator {

	boolean validate(Message message);

}
