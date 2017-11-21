package io.enoy.tg;

import io.enoy.tg.example.TgExamplesConfiguration;
import io.enoy.tg.example.pingpong.PingPong;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import(TgExamplesConfiguration.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableTgBotPingPong {
}