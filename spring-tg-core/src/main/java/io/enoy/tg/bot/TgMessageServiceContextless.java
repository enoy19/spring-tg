package io.enoy.tg.bot;

import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.scope.context.TgContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Telegram message service. Simplifies sending messages.
 *
 * @author Enis Ö.
 */
@Service
@RequiredArgsConstructor
public class TgMessageServiceContextless {
    // TODO: create some non-tg-scoped message service that always requires a user id.
    // TODO: implements every message type that can be sent in tg

    private final TgBot tgBot;

    /**
     * Sends a simple text message to the {@link org.telegram.telegrambots.api.objects.User} in the current {@link TgContext}
     *
     * @param chatId
     * @param message
     * @return Message that was sent to the user
     */
    public Message sendMessage(long chatId, String message) {
        return execute(new SendMessage(chatId, message));
    }

    /**
     * Edits the {@link Message} instance to the given text
     *
     * @param messageText
     * @param message
     */
    public void editMessage(String messageText, Message message) {

        TgContext context = TgContextHolder.currentContext();

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(context.getUserId());
        editMessage.setMessageId(message.getMessageId());
        editMessage.setText(messageText);

        execute(editMessage);
    }

    public Message sendReplyKeyboard(long chatId, String message, List<String> keyboardData, boolean oneTimeKeyboard, int maxColumns) {
        if (maxColumns < 1)
            throw new IllegalArgumentException("maxColumns must be > 0");

        List<List<String>> keyboardText = new ArrayList<>();

        List<String> currentRow = null;

        for (int i = 0; i < keyboardData.size(); i++) {
            if (i % maxColumns == 0) {
                if (Objects.nonNull(currentRow)) {
                    keyboardText.add(currentRow);
                }

                currentRow = new ArrayList<>();
            }

            currentRow.add(keyboardData.get(i));
        }

        if (Objects.nonNull(currentRow) && currentRow.size() <= maxColumns) {
            keyboardText.add(currentRow);
        }

        return sendReplyKeyboard(chatId, message, keyboardText, oneTimeKeyboard);

    }

    public Message sendReplyKeyboard(long chatId, String message, List<List<String>> keyboardText, boolean oneTimeKeyboard) {
        String[][] keyboardTextArray = new String[keyboardText.size()][];

        for (int i = 0; i < keyboardText.size(); i++) {
            List<String> keyboardRow = keyboardText.get(i);

            String[] keyboardRowArray = new String[keyboardRow.size()];
            keyboardTextArray[i] = keyboardRowArray;

            for (int j = 0; j < keyboardRow.size(); j++) {
                keyboardRowArray[j] = keyboardRow.get(j);
            }
        }

        return sendReplyKeyboard(chatId, message, keyboardTextArray, oneTimeKeyboard);
    }

    public Message sendReplyKeyboard(long chatId, String message, String[][] keyboardText, boolean oneTimeKeyboard) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setOneTimeKeyboard(oneTimeKeyboard);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String[] rows : keyboardText) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (String row : rows) {
                keyboardRow.add(row);
            }
            keyboardRows.add(keyboardRow);
        }
        keyboard.setKeyboard(keyboardRows);

        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.setReplyMarkup(keyboard);

        return execute(sendMessage);
    }

    public void removeKeyboard(long chatId) {
        ReplyKeyboardRemove removeMarkup = new ReplyKeyboardRemove();
        SendMessage sendMessage = new SendMessage(chatId, "This message was sent to remove the keyboard.");
        sendMessage.setReplyMarkup(removeMarkup);
        Message message = execute(sendMessage);

        DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
        execute(deleteMessage);
    }

    /**
     * executes the given telegram method. wraps any {@link TelegramApiException} in an {@link IllegalStateException}.
     *
     * @param method the method instance to execute
     * @param <R>    return type
     * @param <T>    method type
     * @return the methods return instance
     * @see #executeWithException(BotApiMethod)
     */
    public <R extends Serializable, T extends BotApiMethod<R>> R execute(T method) {
        try {
            return executeWithException(method);
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * simply executes the given method.
     *
     * @see #execute(BotApiMethod)
     * @see org.telegram.telegrambots.bots.TelegramLongPollingBot#execute(BotApiMethod)
     */
    public <R extends Serializable, T extends BotApiMethod<R>> R executeWithException(T method) throws TelegramApiException {
        return tgBot.execute(method);
    }


}
