package io.enoy.tg.ping;

import io.enoy.tg.action.TgRequest;
import io.enoy.tg.action.TgController;
import io.enoy.tg.action.TgRequestResult;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.api.objects.Message;

@TgController(name = "PingPong", description = "Sends back Pong", regex = "\\/ping")
@Scope("tg")
@RequiredArgsConstructor
public class PingPong {

	private final TgMessageService messageService;

	@TgRequest
	public TgRequestResult pong(Message command) {
		messageService.sendMessage("Pong");
		return TgRequestResult.OK;
	}

}
