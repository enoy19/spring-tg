package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;

import java.util.ArrayList;

/**
 * a concrete class for the {@link java.util.List}<{@link PhotoSize}> that is provided by {@link Message#getPhoto()}
 * @author Enis Ö.
 * @see Message#getPhoto()
 */
public final class TgPhotos extends ArrayList<PhotoSize> {

	public TgPhotos(Message message) {
		super(message.getPhoto().size());
		addAll(message.getPhoto());
	}

}
