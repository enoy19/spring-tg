package io.enoy.tg.action.request;

/**
 * May be returned in any {@link io.enoy.tg.action.TgController} class method annotated with {@link TgRequest} to gain more
 * control over your actions.
 * @author Enis Ö.
 * @see TgRequest
 */
public enum TgRequestResult {
	OK, ABORT, RETRY
}
