package io.enoy.tg.bot.exceptions;

public interface TgExceptionHandler {

	boolean accepts(Throwable e);

	void handle(Throwable e);

}
