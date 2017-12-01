package io.enoy.tg;

import io.enoy.tg.bot.TgBot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfiguration.class})
public class TgBotSetValueTest
{
	@Autowired
	private ApplicationContext context;
	@Value("${bot.token}")
	private String botToken;
	@Value("${bot.name}")
	private String botUsername;

	private TgBot tgBot;

	@Before
	public void setUpTgBot()
	{
		assumeNotNull(context, botToken, botUsername);
		tgBot = new TgBot(context);
	}

	@Test
	public void shouldSetBotToken()
	{
		assertThat(tgBot.getBotToken(), is(botToken));
	}

	@Test
	public void shouldSetBotUsername()
	{
		assertThat(tgBot.getBotUsername(), is(botUsername));
	}
}
