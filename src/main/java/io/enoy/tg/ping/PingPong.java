package io.enoy.tg.ping;

import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.action.TgController;
import io.enoy.tg.action.request.TgRequestResult;
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
	public void pong(String command) {
		System.out.println("THIS WAS A STRING ERMAHGERD!");
		messageService.sendMessage("Pong");
	}

/*	@TgRequest
	public void pong(String command, Message message) {
		System.out.println("THE NEXT MESSAGE");
		System.out.println(message);
		messageService.sendMessage("LOL");
	}*/

}
