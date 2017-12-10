package io.enoy.tg.security;

import io.enoy.tg.bot.TgMessageService;
import io.enoy.tg.bot.exceptions.TgTypedExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Scope("tg")
@RequiredArgsConstructor
public class TgSecurityExceptionHandler implements TgTypedExceptionHandler<AuthenticationException> {

	private final TgMessageService messageService;

	@Override
	public void handleTyped(AuthenticationException e) {
		messageService.sendMessage("Insufficient Permissions!");
	}

	@Override
	public Class<AuthenticationException> getExceptionType() {
		return AuthenticationException.class;
	}

}
