package io.enoy.tg.ping;

import io.enoy.tg.action.TgActionRegexDefinition;
import org.springframework.stereotype.Component;

@Component
public class PingPongActionDefinition implements TgActionRegexDefinition {


	@Override
	public String getName() {
		return "Ping Pong (/ping)";
	}

	@Override
	public String getDescription() {
		return "Test Command";
	}

	@Override
	public String getRegex() {
		return "\\/ping";
	}
}
