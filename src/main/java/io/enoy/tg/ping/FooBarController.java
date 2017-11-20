package io.enoy.tg.ping;

import io.enoy.tg.bot.TgMessageService;
import io.enoy.tg.request.TgRegexRequest;
import io.enoy.tg.scope.context.TgContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FooBarController {

	private final TgMessageService messageService;

	@TgRegexRequest(name="Foobar", description = "Sends back 'bar'", regex = "\\/foo")
	public void foobar() {
		messageService.sendMessage("bar");
	}

}
