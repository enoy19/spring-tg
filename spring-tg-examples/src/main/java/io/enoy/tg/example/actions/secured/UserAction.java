package io.enoy.tg.example.actions.secured;

import io.enoy.tg.action.TgController;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@TgController(name = "User Action", description = "This action can only be used by users", regex = "\\/user")
@RequiredArgsConstructor
public class UserAction {

	private final TgMessageService messageService;

	@TgRequest
	@PreAuthorize("hasRole('ROLE_USER')")
	public void user(String command) {
		messageService.sendMessage("You are a user!");
	}

}
