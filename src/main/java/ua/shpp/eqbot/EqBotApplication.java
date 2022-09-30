package ua.shpp.eqbot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Telegram bot queue",
        version = "${app.version}",
        description = "description"))
@EnableCaching
public class EqBotApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(EqBotApplication.class);

    public static void main(String[] args) {
        LOGGER.info("start application");
        SpringApplication.run(EqBotApplication.class, args);
        LOGGER.info("Congratulations application started");
    }
}
