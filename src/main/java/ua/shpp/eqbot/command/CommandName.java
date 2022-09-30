package ua.shpp.eqbot.command;

public enum CommandName {
    START("/start"),
    REG("/reg"),
    STOP("/stop"),
    HELP("/help"),
    SETTINGS("/settings"),
    ADD_SERVICE("/add"),
    CHANGE_ROLE_TO_PROVIDER("Change role to Provider"),
    DELETE_USER("/delete"),
    NO("nocommand");


    private final String nameCommand;

    CommandName(String commandName) {
        this.nameCommand = commandName;
    }

    public String getNameCommand() {
        return nameCommand;
    }
}
