package ua.shpp.eqbot.dto;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class BotCommandResultDto {
    private boolean isDone;
    private SendMessage message;

    public SendMessage getMessage() {
        return message;
    }

    public BotCommandResultDto setMessage(SendMessage message) {
        this.message = message;
        return this;
    }

    public boolean isDone() {
        return isDone;
    }

    public BotCommandResultDto setDone(boolean done) {
        isDone = done;
        return this;
    }
}
