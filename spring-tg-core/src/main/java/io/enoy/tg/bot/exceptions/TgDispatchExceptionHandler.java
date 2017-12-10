package io.enoy.tg.bot.exceptions;

import io.enoy.tg.bot.TgMessageDispatcher.TgDispatchException;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class TgDispatchExceptionHandler implements TgTypedExceptionHandler<TgDispatchException> {

	private final TgMessageService tgMessageService;

	@Override
	public Class<TgDispatchException> getExceptionType() {
		return TgDispatchException.class;
	}

	@Override
	public void handleTyped(TgDispatchException e) {
		tgMessageService.sendMessage(String.format("Error:%n%s", e.getMessage()));
	}

}
