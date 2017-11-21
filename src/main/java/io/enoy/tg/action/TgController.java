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

	/**
	 * @return the class of the validator of this method. This is processed before using the regex
	 */
	Class<? extends CommandValidator> commandValidator() default NoCommandValidator.class;

	/**
	 * @return if the given message text matches this regex. This controller is a valid candidate. Is only called when commandValidator is NoCommandValidator (default)
	 */
	String regex() default "";

}
