package io.enoy.tg.ping;

import io.enoy.tg.action.TgAction;
import io.enoy.tg.action.TgActionProcessor;
import io.enoy.tg.action.message.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("tg")
@RequiredArgsConstructor
public class PingPongActionProcessor implements TgActionProcessor<PingPongActionDefinition> {

	private final TgMessageService messageService;

	@Override
	public boolean process(TgAction tgAction) {
		messageService.sendMessage("Pong");
		return true; // done!
	}

	@Override
	public Class<PingPongActionDefinition> getDefinitionClass() {
		return PingPongActionDefinition.class;
	}

}
