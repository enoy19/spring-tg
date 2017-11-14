package io.enoy.tg.ping;

import io.enoy.tg.action.SimpleTgActionRegexDefinition;
import io.enoy.tg.action.TgActionContext;
import io.enoy.tg.action.TgActionRegexDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;

@Component
public class PingPongActionDefinition implements SimpleTgActionRegexDefinition {

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
