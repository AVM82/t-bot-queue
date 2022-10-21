package ua.shpp.eqbot.dto;

public class BotCommandResultDto {
    private boolean isDone;

    public boolean isDone() {
        return isDone;
    }

    public BotCommandResultDto setDone(boolean done) {
        isDone = done;
        return this;
    }

}
