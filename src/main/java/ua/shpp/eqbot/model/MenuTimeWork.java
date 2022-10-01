package ua.shpp.eqbot.model;

import ua.shpp.eqbot.commandchain.changerole.RegistrationProviderStep;

import java.util.ArrayList;
import java.util.List;

public enum MenuTimeWork {
    START_WORK_IN_SUNDAY,
    END_WORK_IN_SUNDAY,
    START_WORK_IN_MONDAY,
    END_WORK_IN_MONDAY,
    START_WORK_IN_TUESDAY,
    END_WORK_IN_TUESDAY,
    START_WORK_IN_WEDNESDAY,
    END_WORK_IN_WEDNESDAY,
    START_WORK_IN_THURSDAY,
    END_WORK_IN_THURSDAY,
    START_WORK_IN_FRIDAY,
    END_WORK_IN_FRIDAY,
    START_WORK_IN_SATURDAY,
    END_WORK_IN_SATURDAY,
    TIME_BETWEEN_CLIENTS,
    DONE;


    public MenuTimeWork nextState(MenuTimeWork menuTimeWork) {
        if (menuTimeWork == MenuTimeWork.DONE)
            return menuTimeWork;
        switch (menuTimeWork) {
            case START_WORK_IN_SUNDAY:
                return END_WORK_IN_SUNDAY;
            case END_WORK_IN_SUNDAY:
                return START_WORK_IN_MONDAY;
            case START_WORK_IN_MONDAY:
                return END_WORK_IN_MONDAY;
            case END_WORK_IN_MONDAY:
                return START_WORK_IN_TUESDAY;
            case START_WORK_IN_TUESDAY:
                return END_WORK_IN_TUESDAY;
            case END_WORK_IN_TUESDAY:
                return START_WORK_IN_WEDNESDAY;
            case START_WORK_IN_WEDNESDAY:
                return END_WORK_IN_WEDNESDAY;
            case END_WORK_IN_WEDNESDAY:
                return START_WORK_IN_THURSDAY;
            case START_WORK_IN_THURSDAY:
                return END_WORK_IN_THURSDAY;
            case END_WORK_IN_THURSDAY:
                return START_WORK_IN_FRIDAY;
            case START_WORK_IN_FRIDAY:
                return END_WORK_IN_FRIDAY;

        }
        return DONE;
        }
    }
