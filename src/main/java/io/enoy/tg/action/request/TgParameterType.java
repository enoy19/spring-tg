package io.enoy.tg.action.request;

import org.telegram.telegrambots.api.objects.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public enum TgParameterType {
	MESSAGE(null, m -> m, Message.class::isAssignableFrom, m -> true),
	STRING(MESSAGE, Message::getText, String.class::isAssignableFrom, Message::hasText),
	DOCUMENT(MESSAGE, Message::getDocument, Document.class::isAssignableFrom, Message::hasDocument),
	PHOTO(MESSAGE, Message::getPhoto, TgParameterType::isPhotoList, Message::hasPhoto),
	VOICE(MESSAGE, Message::getVoice, Voice.class::isAssignableFrom, m -> Objects.nonNull(m.getVoice())),
	AUDIO(MESSAGE, Message::getAudio, Audio.class::isAssignableFrom, m -> Objects.nonNull(m.getAudio()));
	// TODO: complete

	private final TgParameterType parent;
	private final Function<Message, Object> dataFunction;
	private final Function<Class<?>, Boolean> typeMatchFunction;
	private final Function<Message, Boolean> messageMatchFunction;

	TgParameterType(TgParameterType parent, Function<Message, Object> dataFunction, Function<Class<?>, Boolean> typeMatchFunction, Function<Message, Boolean> messageMatchFunction) {
		this.parent = parent;
		this.dataFunction = dataFunction;
		this.typeMatchFunction = typeMatchFunction;
		this.messageMatchFunction = messageMatchFunction;
	}

	public TgParameterType getParent() {
		return parent;
	}

	private int getHops(Class<?> type, int currentHops) throws NonMatchingTypeException {
		if (matchesType(type))
			return currentHops;

		if (Objects.nonNull(parent))
			return parent.getHops(type, ++currentHops);

		throw new NonMatchingTypeException();
	}

	public Object getData(Message message) {
		return dataFunction.apply(message);
	}

	public boolean matchesType(Class<?> type) {
		return typeMatchFunction.apply(type);
	}

	public static int getHops(TgParameterType parameterType, Message message) throws NonMatchingTypeException {
		Class<?> type = parameterType.getData(message).getClass();

		TgParameterType matchingType = getMatchingParameterType(message);
		return matchingType.getHops(type, 0);
	}

	public static TgParameterType getMatchingParameterType(Message message) {
		TgParameterType[] values = values();
		for (int i = values.length - 1; i >= 0; i--) {
			TgParameterType value = values[i];
			boolean match = value.messageMatchFunction.apply(message);
			if(match)
				return value;
		}

		throw new ParameterTypeException(String.format("No matching parameter found for message: %s", message.toString()));
	}

	public static TgParameterType getMatchingParameterType(Class<?> type) {
		for (TgParameterType tgParameterType : values()) {
			if (tgParameterType.matchesType(type)) {
				return tgParameterType;
			}
		}
		throw new ParameterTypeException(String.format("Type %s is not defined. Please refer to the values in %s", type.getName(), TgParameterType.class.getName()));
	}

	private static final boolean isPhotoList(Class<?> clazz) {

		if (List.class.isAssignableFrom(clazz)) {
			Type[] genericInterfaces = clazz.getGenericInterfaces();
			if (genericInterfaces.length == 1) {
				Type type = genericInterfaces[0];
				try {
					Class<?> typeClass = Class.forName(type.getTypeName());
					return PhotoSize.class.isAssignableFrom(typeClass);
				} catch (ClassNotFoundException e) {
					// just ignore.
				}
			}
		}

		return false;

	}

	public static class ParameterTypeException extends RuntimeException {
		public ParameterTypeException(String message) {
			super(message);
		}
	}

	public static class NonMatchingTypeException extends Exception {
		public NonMatchingTypeException() {
		}
	}

}
