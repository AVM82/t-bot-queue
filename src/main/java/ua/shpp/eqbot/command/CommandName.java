package ua.shpp.eqbot.command;

public enum CommandName {
    START("/start"),
    REG("/reg"),
    STOP("/stop"),
    HELP("/help"),
    SETTINGS("/settings"),
    ADD_SERVICE("/add"),
    CHANGE_ROLE_TO_PROVIDER("Change role to Provider"),
    REGISTER_NEW_PROVIDER("Реєстрація нового провайдера"),
    NO("nocommand");

    private final String command;

    CommandName(String commandName) {
        this.command = commandName;
    }

    public String getCommand() {
        return command;
    }
}