package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class TgActionContext {

    private final List<Message> inputs;
    private Class<? extends TgActionDefinition<?>> definitionClass;

    public TgActionContext() {
        this.inputs = new ArrayList<>();
    }

    public List<Message> getInputs() {
        return inputs;
    }

    public void setDefinitionClass(Class<? extends TgActionDefinition<?>> definitionClass) {
        this.definitionClass = definitionClass;
    }

    public Class<? extends TgActionDefinition<?>> getDefinitionClass() {
        return definitionClass;
    }

}
