package io.enoy.tg.example.actions;

import io.enoy.tg.action.TgController;
import io.enoy.tg.action.TgPostAction;
import io.enoy.tg.action.TgPreAction;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

@TgController(name = "Favorite Color", description = "Select your favorite color", regex = "\\/favorite_color")
@Scope("tg")
@RequiredArgsConstructor
public class FavoriteColor {

	private final TgMessageService messageService;

	private List<String> colors;

	@TgPreAction
	public void pre() {
		colors = new ArrayList<>();
		colors.add("Red");
		colors.add("Green");
		colors.add("Blue");
	}

	@TgRequest
	public void favColor(String command) {
		messageService.sendReplyKeyboard("Select your favorite color", colors, true, 2);
	}

	@TgRequest
	public void favColor(String command, String color) {
		messageService.sendMessage(String.format("%s is my favorite color too!", color));
	}

	@TgPostAction
	public void post() {
		messageService.removeKeyboard();
	}

}
