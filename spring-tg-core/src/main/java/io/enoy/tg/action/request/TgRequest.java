package io.enoy.tg.action.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a method as a Telegram "request" (message, command... whatever you want to call it).
 * Methods declared with this annotation must be <b>public</b> and have at least one parameter, that is the command itself.
 * Every other argument will be mapped with the user input.<br><br>
 * <b>Examples:</b><br>
 * <ul>
 * <li>public void pingPong(String command) {...}</li>
 * <li>public void addNumbers(String command, String numberA, String numberB) {...}</li>
 * <li>public void savePhotoOnDisk({@link io.enoy.tg.action.TgPhotos} photos) {...}</li>
 * <li>public void playMusicLocally({@link org.telegram.telegrambots.api.objects.Audio} music) {...}</li>
 * </ul>
 * <br>
 * As a message comes to the {@link io.enoy.tg.bot.TgMessageDispatcher} it gets dispatched to the next {@link io.enoy.tg.action.TgAction} which then looks
 * for the best matching {@link TgRequest} for the sequence of arguments you provide.
 * Meaning you can intercept at any given moment of a Telegram command.
 * <br><br>
 * <b>Example:</b><br><br>
 * <ul>
 * <li>public void setLight(String command) { [Some code asking the client for some color] }</li>
 * <li>public void setLight(String command, String colorCode) { [setting the color of some light] }</li>
 * </ul>
 * <br>
 * Using {@link TgRequestResult} you're able to take even more control over your TgRequest. Any TgRequest that returns void is equivalent to {@link TgRequestResult#OK}
 * <br><br>
 * <b>Example:</b><br><br>
 * <ul>
 * <li>public void executeSuperSecretStuff(String command) { [Some code asking the client for a password] }</li>
 * <li>public {@link TgRequestResult} executeSuperSecretStuff(String command, String password) { [checking password and returning some {@link TgRequestResult}] }</li>
 * <li>public {@link TgRequestResult} executeSuperSecretStuff(String command, String password, String secretOperation) { [CORRECT PASSWORD... now do some secret operation] }</li>
 * </ul>
 *
 * @author Enis Ö.
 * @see io.enoy.tg.action.TgActionRegisterer
 * @see TgParameterType
 * @see io.enoy.tg.action.TgController
 * @see TgRequestResult
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TgRequest {

	/**
	 * @return the name of this request. If left as default {@link io.enoy.tg.action.TgActionRegisterer} will use the method name
	 */
	String value() default "";

}
