package io.enoy.tg.bot;

import io.enoy.tg.action.CommandValidator;
import io.enoy.tg.action.TgAction;
import io.enoy.tg.action.TgActionRequestHandler;
import io.enoy.tg.action.ValidType;
import io.enoy.tg.action.request.TgParameterType.NonMatchingTypeException;
import io.enoy.tg.action.request.TgRequestHandlerHopsComposition;
import io.enoy.tg.action.request.TgRequestResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;

import java.util.*;
import java.util.stream.Collectors;

/**
 * routes the messages to the right {@link TgAction} instances and calling the right {@link TgActionRequestHandler}s.
 *
 * @author Enis Ö.
 * @see TgAction
 * @see io.enoy.tg.action.request.TgRequest
 * @see TgActionRequestHandler
 */
@Service
@Scope("tg")
@RequiredArgsConstructor
@Slf4j
public class TgMessageDispatcher {

	private final ApplicationContext context;
	private final Set<TgAction> actions;
	private final List<Message> currentMessages = new ArrayList<>();
	private TgAction currentAction;

	public void dispatch(Message message) throws TgDispatchException {

		Object controllerClass = getControllerClass();

		if (Objects.isNull(currentAction)) {
			currentAction = resolveAction(message);
			// current action was null. must get controller class again
			controllerClass = getControllerClass();
			handlePreAction(controllerClass);
		}

		currentMessages.add(message);

		TgActionRequestHandler handler = resolveHandler();

		TgRequestResult result = TgRequestResult.OK;

		if (Objects.nonNull(handler)) {
			result = handler.execute(controllerClass, currentMessages);
		}

		boolean done = handleResult(result);

		if (done) {
			handlePostAction(controllerClass);
			clear();
		}

	}

	private Object getControllerClass() {
		if (Objects.isNull(currentAction))
			return null;
		return context.getBean(currentAction.getControllerClass());
	}

	private void handlePreAction(Object controllerClass) {
		if (Objects.nonNull(currentAction.getPreAction()))
			currentAction.getPreAction().execute(controllerClass, Collections.emptyList());
	}

	private void handlePostAction(Object controllerClass) {
		if (Objects.nonNull(currentAction.getPostAction()))
			currentAction.getPostAction().execute(controllerClass, Collections.emptyList());
	}

	/**
	 * handles result. clears arguments and current action on done or abort. steps arguments back on retry.
	 *
	 * @param result
	 * @return true if the action is done. false if it is still running
	 */
	private boolean handleResult(TgRequestResult result) {
		switch (result) {
			case RETRY:
				currentMessages.remove(currentMessages.size() - 1);
				break;
			case ABORT:
				return true;
			default:
				if (isAtLastParameter())
					return true;
				break;
		}

		return false;
	}

	public void clear() {
		currentMessages.clear();
		currentAction = null;
	}

	private boolean isAtLastParameter() {
		return currentMessages.size() >= getCurrentActionMaxParameterCount();
	}

	private int getCurrentActionMaxParameterCount() {
		OptionalInt maxParameterCountOptional =
				currentAction.getRequestHandlers().stream()
						.filter(Objects::nonNull)
						.mapToInt(TgActionRequestHandler::getParameterCount)
						.max();

		return maxParameterCountOptional.orElse(-1);
	}

	private TgActionRequestHandler resolveHandler() throws TgDispatchException {
		checkHandlers();

		List<TgRequestHandlerHopsComposition> requestHandlersWithHops = getTgRequestHandlerWithHops();

		if (requestHandlersWithHops.isEmpty())
			return null;

		return getMinimalHopHandler(requestHandlersWithHops);

	}

	private List<TgRequestHandlerHopsComposition> getTgRequestHandlerWithHops() {
		return currentAction.getRequestHandlers().stream()
				.filter(tgActionRequestHandler -> tgActionRequestHandler.getParameterCount() == currentMessages.size())
				.map(tgActionRequestHandler -> {
					try {
						return tgActionRequestHandler.getHops(currentMessages);
					} catch (NonMatchingTypeException e) {
						return null;
					}
				})
				.filter(Objects::nonNull)
				.sorted()
				.collect(Collectors.toList());
	}

	private TgActionRequestHandler getMinimalHopHandler(List<TgRequestHandlerHopsComposition> requestHandlersWithHops) throws TgDispatchException {
		int mostHops = requestHandlersWithHops.stream()
				.mapToInt(TgRequestHandlerHopsComposition::getHops)
				.max()
				.orElseGet(() -> {
					throw new Error("could not gather maximum hop amount. This should never happen wtf...");
				});

		for (int i = 0; i <= mostHops; i++) {
			int finalI = i;
			List<TgRequestHandlerHopsComposition> minHopsHandlers =
					requestHandlersWithHops.stream()
							.filter(tgRequestHandlerHopsComposition -> tgRequestHandlerHopsComposition.getHops() == finalI)
							.collect(Collectors.toList());

			if (minHopsHandlers.size() > 1) {
				throw new TgDispatchException("Multiple handlers found!");
			} else if (minHopsHandlers.size() == 1) {
				return minHopsHandlers.get(0).getHandler();
			}
		}
		return null;
	}

	private void checkHandlers() throws TgDispatchException {
		if (currentAction.getRequestHandlers().isEmpty())
			throw new TgDispatchException("No handlers are defined!");
	}

	private TgAction resolveAction(Message message) throws TgDispatchException {
		List<TgAction> matchingActions = findMatchingActions(message);
		if (matchingActions.size() > 1) {
			log.error("Multiple matching commands found: {}", getCommandNamesJoined(matchingActions));
			throw new TgDispatchException("Multiple matching commands found!");
		} else if (matchingActions.size() == 0) {
			throw new TgDispatchException("Command not found");
		} else {
			return matchingActions.get(0);
		}
	}

	private String getCommandNamesJoined(List<TgAction> matchingActions) {
		return matchingActions.stream().map(TgAction::getName).collect(Collectors.joining(", "));
	}

	private List<TgAction> findMatchingActions(final Message message) {
		return
				actions.stream()
						.filter(action -> {
							ValidType commandValidatorVType = ValidType.NOT_EXISTING;
							ValidType regexVType = ValidType.NOT_EXISTING;

							if (action.isCommandValidatorExisting(message)) {
								CommandValidator validator = getCommandValidator(action.getCommandValidatorClass());
								commandValidatorVType = ValidType.fromBoolean(validator.validate(message));
							}

							if (action.isRegexExisting()) {
								if (message.hasText() && action.hasRegex()) {
									regexVType = ValidType.fromBoolean(action.isRegexMatching(message.getText()));
								}
							}

							return commandValidatorVType.chainType(regexVType).isValid();
						}).collect(Collectors.toList());


	}

	private CommandValidator getCommandValidator(Class<? extends CommandValidator> commandValidatorClass) {
		return context.getBean(commandValidatorClass);
	}

	public static class TgDispatchException extends Exception {
		public TgDispatchException(String message) {
			super(message);
		}
	}

}
