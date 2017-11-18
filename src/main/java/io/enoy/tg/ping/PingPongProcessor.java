package io.enoy.tg.ping;

import io.enoy.tg.action.SimpleTgActionProcessor;
import io.enoy.tg.action.TgActionContext;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("tg")
@RequiredArgsConstructor
public class PingPongProcessor implements SimpleTgActionProcessor<PingPongActionDefinition> {

	private final TgMessageService messageService;

	@Override
	public boolean process(TgActionContext context) {
		messageService.sendMessage("Pong");
		return true; // done!
	}

	@Override
	public Class<PingPongActionDefinition> getActionDefinitionClass() {
		return PingPongActionDefinition.class;
	}

}
