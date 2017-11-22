package io.enoy.tg.scope;

import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The Telegram Scope. It defines how all beans within this scope are created. This scope uses the {@link TgContext} to map the beans.
 * @author Enis Ö.
 * @see TgContextHolder
 * @see TgContext
 */
public class TgScope implements Scope {

	private final Map<TgContext, Map<String, Object>> scopedObjects
			= Collections.synchronizedMap(new HashMap<TgContext, Map<String, Object>>());

	private final Map<TgContext, Map<String, Runnable>> destructionCallbacks
			= Collections.synchronizedMap(new HashMap<TgContext, Map<String, Runnable>>());

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Map<String, Object> scopedObjects = getContextScopedObjects();

		if (!scopedObjects.containsKey(name))
			scopedObjects.put(name, objectFactory.getObject());

		return scopedObjects.get(name);
	}

	@Override
	public Object remove(String name) {
		Map<String, Runnable> destructionCallbacks = getContextDestructionCallbacks();
		Map<String, Object> scopedObjects = getContextScopedObjects();

		destructionCallbacks.remove(name);
		return scopedObjects.remove(name);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable runnable) {
		Map<String, Runnable> destructionCallbacks = getContextDestructionCallbacks();
		destructionCallbacks.put(name, runnable);
	}

	@Override
	public Object resolveContextualObject(String name) {
		return null;
	}

	@Override
	public String getConversationId() {
		return "tg";
	}

	private Map<String, Object> getContextScopedObjects() {
		TgContext context = TgContextHolder.currentContext();

		if (!scopedObjects.containsKey(context)) {
			scopedObjects.put(context, new HashMap<>());
		}

		return scopedObjects.get(context);
	}

	private Map<String, Runnable> getContextDestructionCallbacks() {
		TgContext context = TgContextHolder.currentContext();

		if (!destructionCallbacks.containsKey(context)) {
			destructionCallbacks.put(context, new HashMap<>());
		}

		return destructionCallbacks.get(context);
	}

}
