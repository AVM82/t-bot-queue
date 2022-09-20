package ua.shpp.eqbot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Telegram bot queue",
        version = "${app.version}",
        description = "description"))
public class EqbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EqbotApplication.class, args);
    }

}
