package io.enoy.tg.security;

import io.enoy.tg.scope.context.TgContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@RequiredArgsConstructor
public class TgAuthentication implements Authentication {

	private final TgContext tgContext;
	private final Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public Object getCredentials() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TgContext getDetails() {
		return tgContext;
	}

	@Override
	public Object getPrincipal() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return String.format("%s@%s", tgContext.getUser().getUserName(), tgContext.getChat().getUserName());
	}

}
