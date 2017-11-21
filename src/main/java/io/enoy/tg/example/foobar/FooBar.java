package io.enoy.tg.example.foobar;

import io.enoy.tg.action.TgController;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;

@TgController(name = "FooBar", description = "Sends back Bar", commandValidator = FooBarCommandValidator.class)
@Scope("tg")
@RequiredArgsConstructor
public class FooBar {

	private final TgMessageService messageService;

	@TgRequest
	public void foobar(String command) {
		messageService.sendMessage("bar");
	}

}
