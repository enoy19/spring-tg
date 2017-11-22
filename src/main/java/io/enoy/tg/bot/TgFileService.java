package io.enoy.tg.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.stickers.Sticker;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Utility service that can automatically determine a file id of a any Telegram bot api entity.
 * Can also download the files in a temporary folder
 *
 * @author Enis Ö.
 * @see TgMessageService
 */
@Service
@RequiredArgsConstructor
public class TgFileService {

	@Value("https://api.telegram.org/file/bot${bot.token}/")
	private String botTokenPath;

	private final TgBot tgBot;

	private static String getFileId(Object object) {

		if (object instanceof PhotoSize) {
			return ((PhotoSize) object).getFileId();
		} else if (object instanceof Audio) {
			return ((Audio) object).getFileId();
		} else if (object instanceof Video) {
			return ((Video) object).getFileId();
		} else if (object instanceof VideoNote) {
			return ((VideoNote) object).getFileId();
		} else if (object instanceof Document) {
			return ((Document) object).getFileId();
		} else if (object instanceof Voice) {
			return ((Voice) object).getFileId();
		} else if (object instanceof Sticker) {
			return ((Sticker) object).getFileId();
		} else {
			throw new UnsupportedOperationException(String.format("Object type not supported %s", object.getClass().getName()));
		}

	}

	/**
	 * @see #download(Object, String)
	 */
	public java.io.File download(Object object) {
		return download(object, null);
	}

	public String getFileUrl(String fileId) {
		return String.format("%s%s", botTokenPath, getRelativeFilePath(fileId));
	}

	private String getRelativeFilePath(String fileId) {
		Objects.requireNonNull(fileId);

		GetFile getFileMethod = new GetFile();
		getFileMethod.setFileId(fileId);
		try {
			File file = tgBot.execute(getFileMethod);
			return file.getFilePath();
		} catch (TelegramApiException e) {
			throw new IllegalStateException(e);
		}

	}

	private static void saveFile(String url, java.io.File output) throws IOException {
		URL website = new URL(url);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(output);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}

	/**
	 * @param object allowed types: {@link PhotoSize}, {@link Audio},
	 *               {@link Video}, {@link VideoNote}, {@link Document},
	 *               {@link Voice}, {@link Sticker}
	 * @param suffix the file suffix that should be used when naming
	 * @return a temporary file of the downloaded resource
	 */
	public java.io.File download(Object object, String suffix) {
		String fileId = getFileId(object);
		String fileUrl = getFileUrl(fileId);

		try {
			java.io.File file = Files.createTempFile("telegram-spring-", suffix).toFile();
			saveFile(fileUrl, file);
			return file;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
