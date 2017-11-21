package io.enoy.tg.action;

public final class TgAction {

	private String name;
	private String description;
	private String regex;
	private Class<? extends CommandValidator> commandValidatorClass;
	private TgActionRequestHandler rootHandler;

}
