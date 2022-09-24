package ua.shpp.eqbot.hadlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.shpp.eqbot.messagesender.MessageSender;

@Component
public class CommandHandler {
    Logger log = LoggerFactory.getLogger(CommandHandler.class);

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    private MessageSender messageSender;

    public void checkCommand(Message message) {
        if (message.getText().equals("/start")) {
            log.info("run command start");
            handlerCommandStart();
        } else if (message.getText().equals("/stop")) {
            log.info("run command stop");
            handlerCommandStop();
        } else if (message.getText().equals("/help")) {
            log.info("run command help");
            handlerCommandHelp();
        }
    }

    private void handlerCommandHelp() {
    }

    private void handlerCommandStop() {
    }

    private void handlerCommandStart() {
    }
}
