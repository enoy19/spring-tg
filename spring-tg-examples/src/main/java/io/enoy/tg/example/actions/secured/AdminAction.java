package io.enoy.tg.example.actions.secured;

import io.enoy.tg.action.TgController;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@TgController(name = "Admin Action", description = "This action can only be used by admins", regex = "\\/admin")
@RequiredArgsConstructor
public class AdminAction {

	private final TgMessageService messageService;

	@TgRequest
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void admin(String command) {
		messageService.sendMessage("You are an admin!");
	}

}
