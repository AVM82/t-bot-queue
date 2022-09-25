package ua.shpp.eqbot.telegrambot.command;

public enum CommandName {
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    SETTINGS("/settings"),

    CHANGE_ROLE_TO_PROVIDER("Change role to Provider"),

    REGISTR_NEW_PROVIDER("Реестрація нового провайдера"),

    NO("nocommand");


    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}