package io.enoy.tg.example.pingpong;

import io.enoy.tg.action.TgController;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;

@TgController(name = "PingPong", description = "Sends back Pong", regex = "\\/pingpong")
@Scope("tg")
@RequiredArgsConstructor
public class PingPong {

	private final TgMessageService messageService;

	@TgRequest
	public void pong(String command) {
		messageService.sendMessage("Pong");
	}

}
