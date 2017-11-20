package io.enoy.tg.request;

import io.enoy.tg.action.TgActionContext;
import io.enoy.tg.action.TgActionContextFactory;
import org.telegram.telegrambots.api.objects.Message;

public class SimpleTgActionContextFactory implements TgActionContextFactory<TgActionContext> {

	@Override
	public TgActionContext createContext(Message message) {
		return new TgActionContext();
	}

}
