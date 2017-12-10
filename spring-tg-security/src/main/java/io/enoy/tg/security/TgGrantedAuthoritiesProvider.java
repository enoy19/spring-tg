package io.enoy.tg.security;

import io.enoy.tg.scope.context.TgContext;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface TgGrantedAuthoritiesProvider {

	Collection<GrantedAuthority> getAuthoritiesOf(TgContext tgContext);

}
