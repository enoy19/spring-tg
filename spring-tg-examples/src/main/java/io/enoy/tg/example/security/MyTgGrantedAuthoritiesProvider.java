package io.enoy.tg.example.security;

import io.enoy.tg.scope.context.TgContext;
import io.enoy.tg.security.TgGrantedAuthoritiesProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MyTgGrantedAuthoritiesProvider implements TgGrantedAuthoritiesProvider {

	/**
	 * the username list of admins
	 */
	@Value("#{'${my.tg.security.admins}'.split(',')}")
	private List<String> admins;

	/**
	 * the username list of users
	 */
	@Value("#{'${my.tg.security.users}'.split(',')}")
	private List<String> users;

	private static GrantedAuthority getAuthorityInstance(String role) {
		return new SimpleGrantedAuthority(String.format("ROLE_%s", role));
	}

	@Override
	public Collection<GrantedAuthority> getAuthoritiesOf(TgContext tgContext) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		boolean isAdmin = admins.contains(tgContext.getUser().getUserName());
		boolean isUser = users.contains(tgContext.getUser().getUserName());

		if (isAdmin) {
			authorities.add(getAuthorityInstance("ADMIN"));
		}

		if (isUser || isAdmin) {
			authorities.add(getAuthorityInstance("USER"));
		}

		return authorities;
	}

}
