package ua.shpp.eqbot.commandchain.changerole;

public enum RegistrationProviderStep {
    REG_MESSAGE_STEP,
    REG_ADD_NAME_STEP,
    REG_ADD_CITY_STEP,
    REG_DONE;
    public RegistrationProviderStep next (RegistrationProviderStep registrationProviderStep){
        switch (registrationProviderStep){
            case REG_MESSAGE_STEP:
                return REG_ADD_NAME_STEP;
            case REG_ADD_NAME_STEP:
                return REG_ADD_CITY_STEP;
            case REG_ADD_CITY_STEP:
                return REG_DONE;
            default:
                return REG_DONE;
        }
    }
}
