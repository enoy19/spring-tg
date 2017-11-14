package io.enoy.tg.action;

public interface SimpleTgActionProcessor<D extends TgActionDefinition<TgActionContext>> extends TgActionProcessor<TgActionContext, D> {

    @Override
    default Class<TgActionContext> getActionContextClass() {
        return TgActionContext.class;
    }

}
