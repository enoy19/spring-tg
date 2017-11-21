package io.enoy.tg.action;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.api.objects.Message;

import java.lang.reflect.Method;
import java.util.List;

@Getter
@Setter
public final class TgActionRequestHandler {

	private Class<?> paramType;
	private Method method;
	private TgActionRequestHandler parent;
	private List<TgActionRequestHandler> children;

	public TgRequestResult execute(List<Message> messages) {

		return TgRequestResult.OK;
	}

}
