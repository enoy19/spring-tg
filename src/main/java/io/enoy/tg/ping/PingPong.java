package io.enoy.tg.ping;

import io.enoy.tg.action.TgController;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.action.request.TgRequestResult;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.api.objects.Voice;

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

	@TgRequest
	public TgRequestResult pong(String command, String apfel) {
		messageService.sendMessage("Pong: " + apfel);

		if (apfel.equalsIgnoreCase("a")) {
			messageService.sendMessage("RETRY!");
			return TgRequestResult.RETRY;
		}

		return TgRequestResult.OK;
	}

	@TgRequest
	public void pong(String command, Voice voice) {
		messageService.sendMessage("Oh you sent a voice!");
	}

/*	@TgRequest
	public void pong(String command, Message message) {
		System.out.println("THE NEXT MESSAGE");
		System.out.println(message);
		messageService.sendMessage("LOL");
	}*/

}
