package ua.shpp.eqbot.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class CommandUtils {

    public static final String BOT_COMMAND = "BotCommand";

    private CommandUtils() {
        /* intentionally */
    }

    public static String buildFirstLowerClassName(String value) {
        String commandClassName = buildCommandClassName(value);
        String firstChar = commandClassName.substring(0, 1)
                .toLowerCase();
        return firstChar + StringUtils.substring(commandClassName, 1);
    }


    public static String buildCommandClassName(String botCommand) {
        String substring = StringUtils.trimToEmpty(StringUtils.substring(botCommand, 1))
                .toLowerCase()
                .replace("_", " ");
        if (StringUtils.isBlank(substring) || !StringUtils.isAlphaSpace(substring)) {
            return "Unknown" + BOT_COMMAND;
        }
        String[] words = substring.split(" ");
        return Arrays.stream(words)
                .map(StringUtils::capitalize)
                .collect(Collectors.joining())
                + BOT_COMMAND;
    }
}
