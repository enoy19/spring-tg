package io.enoy.tg.example.actions;

import io.enoy.tg.action.TgController;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;

@TgController(name = "Add", description = "Add two numbers together", regex = "\\/add")
@RequiredArgsConstructor
public class AddAction {

	private final TgMessageService messageService;

	@TgRequest
	public void add(String command, String numberOne, String numberTwo) {
		double valueOne = Double.parseDouble(numberOne);
		double valueTwo = Double.parseDouble(numberTwo);
		double answer = valueOne + valueTwo;

		messageService.sendMessage("Answer: " + answer);
	}

}
