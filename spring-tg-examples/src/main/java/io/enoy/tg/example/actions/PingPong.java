package io.enoy.tg.example.actions;

import io.enoy.tg.action.TgController;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;

@TgController(name = "PingPong", description = "Sends back Pong", regex = "\\/ping")
@RequiredArgsConstructor
public class PingPong {

	private final TgMessageService messageService;

	@TgRequest
	public void pong(String command) {
		messageService.sendMessage("Pong");
	}

}
