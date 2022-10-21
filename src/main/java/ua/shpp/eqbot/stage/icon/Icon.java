package ua.shpp.eqbot.stage.icon;

import com.vdurmont.emoji.EmojiParser;

/**
 * <a href="https://github.com/vdurmont/emoji-java/blob/master/EMOJIS.md">List smiles</a>
 */
public enum Icon {
    ARROW_DOWN(":arrow_down:"),
    POINT_DOWN(":point_down:"),
    RED_TRIANGLE(":small_red_triangle_down:"),
    EXCLAMATION(":exclamation:"),
    NOT(":x:");

    private final String value;

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

    Icon(String value) {
        this.value = value;
    }
}
