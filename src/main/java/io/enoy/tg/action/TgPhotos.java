package io.enoy.tg.action;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;

import java.util.ArrayList;

public final class TgPhotos extends ArrayList<PhotoSize> {

	public TgPhotos(Message message) {
		super(message.getPhoto().size());
		addAll(message.getPhoto());
	}

}
