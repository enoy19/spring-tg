package io.enoy.tg.action;

public interface TgActionProcessor<T extends TgActionDefinition> {

	/**
	 * @return true if the action is done, false if the user must input more information...
	 */
	boolean process(TgAction tgAction);
	Class<T> getDefinitionClass();

}
