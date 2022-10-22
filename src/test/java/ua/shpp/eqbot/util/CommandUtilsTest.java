package ua.shpp.eqbot.util;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import ua.shpp.eqbot.command.LogBotCommand;
import ua.shpp.eqbot.command.UnknownBotCommand;

import static org.hamcrest.MatcherAssert.assertThat;

class CommandUtilsTest {
    @Test
    void whenCommandThenJavaClass() {
        assertThat(CommandUtils.buildCommandClassName("/log"), Is.is(LogBotCommand.class.getSimpleName()));
    }

    @Test
    void whenCommandEmptyThenJavaClassNoCommand() {
        assertThat(CommandUtils.buildCommandClassName(""), Is.is(UnknownBotCommand.class.getSimpleName()));
        assertThat(CommandUtils.buildCommandClassName(null), Is.is(UnknownBotCommand.class.getSimpleName()));
        assertThat(CommandUtils.buildCommandClassName("/"), Is.is(UnknownBotCommand.class.getSimpleName()));
        assertThat(CommandUtils.buildCommandClassName("/ "), Is.is(UnknownBotCommand.class.getSimpleName()));
        assertThat(CommandUtils.buildCommandClassName(" "), Is.is(UnknownBotCommand.class.getSimpleName()));
        assertThat(CommandUtils.buildCommandClassName("/1"), Is.is(UnknownBotCommand.class.getSimpleName()));
    }

    @Test
    void whenCommandTwiceThenJavaNoCommand() {
        assertThat(CommandUtils.buildCommandClassName("/abc qwe"), Is.is("AbcQweBotCommand"));
        assertThat(CommandUtils.buildCommandClassName("/abc  qwe "), Is.is("AbcQweBotCommand"));
    }

    @Test
    void whenCommandUnderscoreThenJavaNoCommand() {
        assertThat(CommandUtils.buildCommandClassName("/abc_qwe"), Is.is("AbcQweBotCommand"));
        assertThat(CommandUtils.buildCommandClassName("/abc__qwe_"), Is.is("AbcQweBotCommand"));
    }

    @Test
    void buildFirstLowerClassName() {
        assertThat(CommandUtils.buildFirstLowerClassName("/Acb"), Is.is("acbBotCommand"));
    }

    @Test
    void whenCommandFirstCharNoSlashThenNoFindCommand() {
        assertThat(CommandUtils.buildCommandClassName("/mainMenu"), Is.is("MainmenuBotCommand"));
    }
}
