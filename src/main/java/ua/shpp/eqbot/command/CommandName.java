package ua.shpp.eqbot.command;

public enum CommandName {
    START("/start"),
    REG("/reg"),
    STOP("/stop"),
    HELP("/help"),
    SETTINGS("/settings"),
    ADD_SERVICE("/add"),
    SEARCH_SERVICE("/search"),
    SEARCH_MENU("/searchMenu"),
    CHANGE_ROLE_TO_PROVIDER("/change_role"),
    CHANGE_LANGUAGE("/change_language"),
    DELETE_USER("/delete"),
    MAIN_MENU("mainMenu"),
    ADD_PROVIDER("/add provider"),
    CHECK_PROVIDER("/check provider"),
    CHECK_SERVICE("/check service"),
    NO("nocommand");

    private final String nameCommand;

    CommandName(String commandName) {
        this.nameCommand = commandName;
    }

    public String getNameCommand() {
        return nameCommand;
    }
}
