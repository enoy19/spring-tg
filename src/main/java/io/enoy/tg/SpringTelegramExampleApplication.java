package io.enoy.tg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableTgBot
public class SpringTelegramExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTelegramExampleApplication.class, args);
    }

}
