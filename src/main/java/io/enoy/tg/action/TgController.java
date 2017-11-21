package io.enoy.tg.action;

import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Controller
public @interface TgController {

	String name();
	String description();
	String regex() default "";
	Class<? extends CommandValidator> commandValidator() default NoCommandValidator.class;

}
