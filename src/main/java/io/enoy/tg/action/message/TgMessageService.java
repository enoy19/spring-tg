package io.enoy.tg.action.message;

import io.enoy.tg.bot.TgBot;
import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Objects;

@Service
@Scope("tg")
@RequiredArgsConstructor
public class TgMessageService {

    @Value("https://api.telegram.org/file/bot${bot.token}/")
    private String botTokenPath;

    private final TgBot tgBot;

    public void sendMessage(String message) {

        TgContext context = TgContextHolder.currentContext();
        try {
            tgBot.execute(new SendMessage(context.getUserId(), message));
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e);
        }

    }

    public String getRelativeFilePath(PhotoSize photo) {
        Objects.requireNonNull(photo);

        if (photo.hasFilePath()) {
            return photo.getFilePath();
        } else {
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(photo.getFileId());
            try {
                File file = tgBot.execute(getFileMethod);
                return file.getFilePath();
            } catch (TelegramApiException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public String getFilePath(PhotoSize photo) {
        return String.format("%s%s", botTokenPath, getRelativeFilePath(photo));
    }

}
