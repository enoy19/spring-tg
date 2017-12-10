package io.enoy.tg.example.actions;

import com.google.common.io.Files;
import io.enoy.tg.action.TgController;
import io.enoy.tg.action.TgPhotos;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.bot.TgFileService;
import io.enoy.tg.bot.TgMessageService;
import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.api.objects.PhotoSize;

import java.io.File;
import java.io.IOException;

@TgController(name = "Save Image", description = "Saves any image to the disk", commandValidator = SaveImageCommandValidator.class)
@RequiredArgsConstructor
public class SaveImage {

	private final TgMessageService messageService;
	private final TgFileService fileService;

	@TgRequest
	public void saveImage(TgPhotos photos) {
		PhotoSize largest = photos.get(photos.size() - 1);
		File tmpFile = fileService.download(largest);

		TgContext tgContext = TgContextHolder.currentContext();
		String dirName = String.format("./%d_%s", tgContext.getUser().getId(), tgContext.getUser().getUserName());

		File dir = new File(dirName);
		if (!dir.exists())
			if (!dir.mkdirs())
				throw new IllegalStateException("Could not create directory");

		File file = new File(dir, tmpFile.getName());

		try {
			Files.copy(tmpFile, file);
			messageService.sendMessage("File saved!");
		} catch (IOException e) {
			messageService.sendMessage("Failed to save file!");
			throw new IllegalStateException(e);
		}

	}

}
