package io.enoy.tg.action;

public interface TgActionProcessor<T extends TgActionContext, D extends TgActionDefinition<T>> {

	/**
	 * @return true if the action is done, false if the user must input more information...
	 */
	boolean process(T tgAction);
	Class<T> getActionContextClass();
	Class<D> getActionDefinitionClass();

}
