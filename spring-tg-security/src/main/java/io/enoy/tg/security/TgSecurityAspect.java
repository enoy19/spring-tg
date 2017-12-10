package io.enoy.tg.security;

import io.enoy.tg.scope.context.TgContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class TgSecurityAspect {

	private final Optional<TgGrantedAuthoritiesProvider> tgGrantedAuthoritiesProviderOptional;

	@Before("execution(* io.enoy.tg.bot.TgBotMessageHandler.handleMessage(..))")
	public void setupSecurityContext(JoinPoint joinPoint) {
		TgContext tgContext = (TgContext) joinPoint.getArgs()[1];

		Collection<GrantedAuthority> tgGrantedAuthorities = getGrantedAuthorities(tgContext);
		TgAuthentication tgAuthentication = new TgAuthentication(tgContext, tgGrantedAuthorities);
		SecurityContextHolder.getContext().setAuthentication(tgAuthentication);
	}

	private Collection<GrantedAuthority> getGrantedAuthorities(TgContext tgContext) {
		if (tgGrantedAuthoritiesProviderOptional.isPresent())
			return tgGrantedAuthoritiesProviderOptional.get().getAuthoritiesOf(tgContext);

		return Collections.emptyList();
	}

}
