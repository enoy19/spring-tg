package io.enoy.tg.request;

import io.enoy.tg.action.TgActionContextFactory;
import io.enoy.tg.action.TgActionRegexDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TgRegexRequest {

	Class<? extends TgActionRegexDefinition<?>> value() default NoTgActionRegexDefinition.class;
	Class<? extends TgActionContextFactory<?>> contextFactory() default SimpleTgActionContextFactory.class;

	String name() default "";
	String description() default "";
	String regex() default "";

}
