package io.enoy.tg.example.actions.secured;

import io.enoy.tg.action.TgController;
import io.enoy.tg.action.request.TgRequest;
import io.enoy.tg.bot.TgMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@TgController(name = "Admin or User Action", description = "This action can be used by admins and users", regex = "\\/admin_user")
@RequiredArgsConstructor
public class UserOrAdminAction {

	private final TgMessageService messageService;

	@TgRequest
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public void adminOrUser(String command) {
		messageService.sendMessage("You are a user or an admin!");
	}

}
