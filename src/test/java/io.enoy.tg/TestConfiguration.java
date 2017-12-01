package io.enoy.tg;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource(locations = "classpath:/application.yml")
public class TestConfiguration
{
}
