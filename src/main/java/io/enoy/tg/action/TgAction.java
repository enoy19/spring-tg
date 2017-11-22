package io.enoy.tg.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.api.objects.Message;

import java.util.List;
import java.util.Objects;

/**
 * a class that unites the data of {@link TgController} and {@link io.enoy.tg.action.request.TgRequest}
 * and provides some convenient methods.
 * @author Enis Ö.
 * @see TgController
 * @see CommandValidator
 * @see TgActionRequestHandler
 */
@Getter
@RequiredArgsConstructor
public final class TgAction {

	private final Class<?> controllerClass;
	private final String name;
	private final String description;
	private final String regex;
	private final Class<? extends CommandValidator> commandValidatorClass;
	private final List<TgActionRequestHandler> requestHandlers;

	public boolean hasRegex() {
		return Objects.nonNull(regex) && !regex.trim().isEmpty();
	}

	public boolean isCommandValidatorExisting(Message message) {
		Class<? extends CommandValidator> commandValidatorClass = getCommandValidatorClass();
		return !commandValidatorClass.equals(NoCommandValidator.class);
	}

	public boolean isRegexMatching(String text) {
		return text.matches(regex);
	}

}
