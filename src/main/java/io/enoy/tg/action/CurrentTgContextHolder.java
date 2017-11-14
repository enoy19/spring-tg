package io.enoy.tg.action;

import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Scope("tg")
public class CurrentTgContextHolder {

    private TgContext currentContext;

    @PostConstruct
    private void init() {
        currentContext = TgContextHolder.currentContext();
    }

    public TgContext getCurrentContext() {
        return currentContext;
    }

}
