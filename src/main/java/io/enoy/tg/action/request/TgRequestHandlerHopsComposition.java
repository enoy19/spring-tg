package io.enoy.tg.action.request;

import io.enoy.tg.action.TgActionRequestHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
