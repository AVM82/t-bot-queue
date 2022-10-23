package ua.shpp.eqbot.command;

public enum CommandName {
    START("/start"),
    REG("/reg"),
    STOP("/stop"),
    HELP("/help"),
    SETTINGS("/settings"),
    ADD_SERVICE("/add"),
    SEARCH_SERVICE_BY_CITY_NAME("/search"),
    SEARCH_MENU("/searchMenu"),
    SEARCH_BY_ID("/searchId"),
    SEARCH_USES_NAME_SERVICE("/searchString"),
    CHANGE_ROLE_TO_PROVIDER("/change_role"),
    CHANGE_LANGUAGE("/change_language"),
    DELETE_USER("/delete"),
    MAIN_MENU("/mainMenu"),
    ADD_PROVIDER("/add provider"),
    CHECK_PROVIDER("/check provider"),
    CHECK_SERVICE("/check service"),
    REGISTRATION_FOR_THE_SERVICE_COMMAND("/RegistrationForTheServiceCommand"),
    SERVICE_INFO("/service info"),
    FEEDBACK("/feedback"),
    RECORD_YOUR_USER("/RecordYourUser"),
    NO("nocommand"),
    BLACKLIST("blacklist");

    private final String nameCommand;

    CommandName(String commandName) {
        this.nameCommand = commandName;
    }

    public String getNameCommand() {
        return nameCommand;
    }
}
