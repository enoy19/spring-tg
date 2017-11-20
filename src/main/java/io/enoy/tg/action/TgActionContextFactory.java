package io.enoy.tg.action;

import io.enoy.tg.action.TgActionContext;
import org.telegram.telegrambots.api.objects.Message;

@FunctionalInterface
public interface TgActionContextFactory<T extends TgActionContext> {

	T createContext(Message message);

}
