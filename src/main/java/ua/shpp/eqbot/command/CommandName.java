package ua.shpp.eqbot.command;

public enum CommandName {
    START("/start"),
    REG("/reg"),
    STOP("/stop"),
    HELP("/help"),
    SETTINGS("/settings"),
    ADD_SERVICE("/add"),

    CHANGE_ROLE_TO_PROVIDER("Change role to Provider"),

    REGISTR_NEW_PROVIDER("реєстрація нового провайдера"),

    NO("nocommand");


    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}