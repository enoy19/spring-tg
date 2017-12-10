package io.enoy.tg.action.request;

import io.enoy.tg.action.TgActionRequestHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.api.objects.Message;

/**
 * A model class to group {@link TgActionRequestHandler} and hop count
 * @author Enis Ö.
 * @see TgParameterType
 * @see TgParameterType#getHops(Class, int)
 * @see TgParameterType#getHops(TgParameterType, Message)
 * @see io.enoy.tg.bot.TgMessageDispatcher
 */
@Getter
@RequiredArgsConstructor
public class TgRequestHandlerHopsComposition implements Comparable<TgRequestHandlerHopsComposition> {

	private final TgActionRequestHandler handler;
	private final int hops;

	@Override
	public int compareTo(TgRequestHandlerHopsComposition o) {
		return Integer.compare(this.hops, o.hops);
	}
}
