package io.enoy.tg.action.request;

import io.enoy.tg.action.TgPhotos;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.stickers.Sticker;

import java.util.Objects;
import java.util.function.Function;

// TODO: benefits of restructuring this enum concept to extended classes... think about it!

/**
 * defines the available data in a {@link Message} that can be mapped in {@link TgRequest}s as methods.
 * @author Enis Ö.
 * @see TgRequest
 */
public enum TgParameterType {
	MESSAGE(null, m -> m, Message.class::isAssignableFrom, m -> true),
	STRING(MESSAGE, Message::getText, String.class::isAssignableFrom, Message::hasText),
	DOCUMENT(MESSAGE, Message::getDocument, Document.class::isAssignableFrom, Message::hasDocument),
	PHOTO(MESSAGE, TgPhotos::new, TgPhotos.class::isAssignableFrom, Message::hasPhoto),
	VOICE(MESSAGE, Message::getVoice, Voice.class::isAssignableFrom, m -> Objects.nonNull(m.getVoice())),
	LOCATION(MESSAGE, Message::getLocation, Location.class::isAssignableFrom, Message::hasLocation),
	STICKER(MESSAGE, Message::getSticker, Sticker.class::isAssignableFrom, m -> Objects.nonNull(m.getSticker())),
	VIDEO(MESSAGE, Message::getVideo, Video.class::isAssignableFrom, m -> Objects.nonNull(m.getVideo())),
	CONTACT(MESSAGE, Message::getContact, Contact.class::isAssignableFrom, m -> Objects.nonNull(m.getContact())),
	VIDEO_NOTE(MESSAGE, Message::getVideoNote, VideoNote.class::isAssignableFrom, m -> Objects.nonNull(m.getVideoNote())),
	AUDIO(MESSAGE, Message::getAudio, Audio.class::isAssignableFrom, m -> Objects.nonNull(m.getAudio()));

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
		Object data = parameterType.getData(message);

		if (Objects.isNull(data))
			throw new NonMatchingTypeException();

		Class<?> type = data.getClass();

		TgParameterType matchingType = getMatchingParameterType(message);
		return matchingType.getHops(type, 0);
	}

	public static TgParameterType getMatchingParameterType(Message message) {
		TgParameterType[] values = values();
		for (int i = values.length - 1; i >= 0; i--) {
			TgParameterType value = values[i];
			boolean match = value.messageMatchFunction.apply(message);
			if (match)
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
