package ua.shpp.eqbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class BotConfig {

    //check properties file parameters name and token
    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

    public BotConfig() {
    }

    public String getBotName() {
        return botName;
    }

    public String getToken() {
        return token;
    }

}
