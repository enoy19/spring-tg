package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class TgAction {

	private List<Message> inputs;
	private Class<? extends TgActionDefinition> definitionClass;

	public TgAction(Class<? extends TgActionDefinition> definitionClass) {
		this.inputs = new ArrayList<>();
		this.definitionClass = definitionClass;
	}

	public Class<? extends TgActionDefinition> getDefinitionClass() {
		return definitionClass;
	}

	public List<Message> getInputs() {
		return inputs;
	}

}
