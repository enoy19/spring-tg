package io.enoy.tg.bot.exceptions;

public interface TgTypedExceptionHandler<T extends Throwable> extends TgExceptionHandler {

	@Override
	default boolean accepts(Throwable e) {
		return getExceptionType().isAssignableFrom(e.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	default void handle(Throwable e) {
		handleTyped((T) e);
	}

	void handleTyped(T e);

	Class<T> getExceptionType();
}
