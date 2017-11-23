# spring-tg
Create [Telegram Bot](https://core.telegram.org/bots) commands within [Spring](https://spring.io/) in a SpringMVC manner.
This project uses [rubenlagus'](https://github.com/rubenlagus) [Java implementation](https://github.com/rubenlagus/TelegramBots) of the [Telegram](https://telegram.org/) [Bot API](https://core.telegram.org/bots/api)

## Examples

For an example spring boot project have a look at: [enoy19/spring-tg-examples](https://github.com/enoy19/spring-tg-examples)

##### Ping pong example:
```java
@TgController(name = "PingPong", description = "Sends back Pong", regex = "\\/ping") // describe an action
public class PingPong {

    @Autowired
    private TgMessageService messageService;

    @TgRequest // marks an action handler for a specific order of arguments
    public void pong(String command) {
        messageService.sendMessage("Pong");
    }

}
```

##### Another Example with more than one argument:
```java
@TgController(name = "Add", description = "Add two numbers together", regex = "\\/add")
public class AddAction {
	
    @Autowired
    private TgMessageService messageService;
    
    @TgRequest
    public void add(String command, String numberOne, String numberTwo) {
        double valueOne = Double.parseDouble(numberOne);
        double valueTwo = Double.parseDouble(numberTwo);
        double answer = valueOne + valueTwo;
        
        messageService.sendMessage("Answer: " + answer);
    }

}
```

##### You may also intercept between each argument:
```java
@TgController(name = "Add", description = "Add two numbers together", regex = "\\/add")
public class AddAction {
    
    @Autowired
    private TgMessageService messageService;
    
    @TgRequest
    public void add(String command) {
        messageService.sendMessage("Please enter your first number.");
    }
    
    @TgRequest
    public void add(String command, String numberOne) {
        messageService.sendMessage("Your first number was: " + numberOne);
        messageService.sendMessage("Please enter your second number.");
    }
    
    @TgRequest
    public void add(String command, String numberOne, String numberTwo) {
    	messageService.sendMessage("Your second number was: " + numberTwo);
    	
        double valueOne = Double.parseDouble(numberOne);
        double valueTwo = Double.parseDouble(numberTwo);
        double answer = valueOne + valueTwo;
    
        messageService.sendMessage("Answer: " + answer);
    }

}
```

##### Telegram User Scope: @Scope("tg")
```java
@TgController(name = "Concatenate", description = "Concatenate a string infinitely", regex = "\\/concat")
@Scope("tg") // scoped only to one user
public class AddAction {
    
    @Autowired
    private TgMessageService messageService;
    
    private String userText = ""; // this userText exists for every Telegram user, since this bean is scoped by the user
    
    @TgRequest
    public void concat(String command) {
        messageService.sendMessage("Please write any text. Enter 'DONE' when you want to see the results");
    }
        
    @TgRequest
    public void concat(String command, String text) {
    	if(text.equals("DONE")) {
    		messageService.sendMessage(userText);
    		userText = "";
    	} else {
    		userText += text;
            messageService.sendMessage("You added: " + text);
    	}
    }

}
```

##### More Control
```java
@TgController(name = "Guess", description = "Guess a random number between 1-10", regex = "\\/guess") 
@Scope("tg")
public class PingPong {

    private static final Random RANDOM = new Random();
    private static final int MAX_RETRIES = 5;

    @Autowired
    private TgMessageService messageService;
    
    private int randomNumber;
    private int retryCounter;

    @TgRequest
    public void guess(String command) {
        messageService.sendMessage("Make your guess...");
        retryCounter = 0;
        randomNumber = RANDOM.nextInt(10) + 1;
    }
    
    @TgRequest
    public TgRequestResult guess(String command, String guessString) {
    	int guess = Integer.parseInt(guessString);
    	
    	if(guess == randomNumber) {
    		messageService.sendMessage("You got it!");
    		return TgRequestResult.OK;
    	} else {
    		messageService.sendMessage("Wrong, try again.");
    		retryCounter++;
            return TgRequestResult.RETRY;
    	}
    	
    	if(retryCounter >= MAX_RETRIES) {
    		messageService.sendMessage("Too many tries!");
    		return TgRequestResult.ABORT;
    	}
    	
    }
    
    @TgRequest
    public void guess(String command, String guessString, String wish) {
    	messageService.sendMessage("I am sorry but I can't make your wish '" + wish + "' come true :(");
    }

}
```

##### Any Telegram message data type
```java
@TgController(name = "Play Music", description = "Plays the given song", regex = "\\/play_music") 
public class PingPong {

    @Autowired
    private TgMessageService messageService;

    @TgRequest
    public void playMusic(String command) {
        messageService.sendMessage("Please send a song");
    }

    @TgRequest
    public void playMusic(String command, org.telegram.telegrambots.api.objects.Audio audio) {
        play(audio);
    }
    
    private void play(org.telegram.telegrambots.api.objects.Audio audio) {
        // play
    }


}
```

##### Resolving message data types
```java
@TgController(name = "Play Music", description = "Plays the given song", regex = "\\/play_music") 
public class PingPong {

    @Autowired
    private TgMessageService messageService;

    @TgRequest
    public void playMusic(String command) {
        messageService.sendMessage("Please send a song");
    }

    /**
    * Gets invoked when an {@link org.telegram.telegrambots.api.objects.Audio} is received as a second argument
    */
    @TgRequest
    public void playMusic(String command, org.telegram.telegrambots.api.objects.Audio audio) {
        playAudio(audio);
    }
    
    /**
    * Gets invoked when an {@link org.telegram.telegrambots.api.objects.Voice} is received as a second argument
    */
    @TgRequest
    public void playMusic(String command, org.telegram.telegrambots.api.objects.Voice voice) {
    	playVoice(voice);
    }
    
    /**
    * Gets invoked when any other message is received as a second argument
    */
    @TgRequest
    public void playMusic(String command, org.telegram.telegrambots.api.objects.Message message) {
    	messageService.sendMessage("Invalid message. Please send Audio or Voice.");
    }
    
    private void playAudio(org.telegram.telegrambots.api.objects.Audio audio) {
        // play audio
    }
    
    private void playVoice(org.telegram.telegrambots.api.objects.Voice voice) {
    	// play voice
    }


}
```

##### Custom logic for command validation (instead of regex)
```java
// set a class for commandValidator to use a custom CommandValidator
@TgController(name = "Save Image", description = "Saves an image to the disk", commandValidator = SaveImageCommandValidator.class ) 
public class SaveImage {

    @Autowired
    private TgMessageService messageService;

    @TgRequest
    public void pong(TgPhotos photos) {
        messageService.sendMessage("Saving photo...");
        savePhoto(photos.get( photos.size() - 1 ));
    }
    
    private void savePhoto(org.telegram.telegrambots.api.objects.PhotoSize photo) {
    	// save photo
    }

}

@Component
public class SaveImageCommandValidator implements CommandValidator {
	
	public boolean validate(Message message) {
		return message.hasPhoto();
	}
	
}
```
