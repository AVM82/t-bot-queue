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
    CRY(":cry:"),
    FLUSHED(":flushed:"),
    HEART_EYES(":heart_eyes:"),
    HEARTPULSE(":heartpulse:"),
    MAN_RAISING_HAND(":raising_hand:"),
    INCOMING_ENVELOPE(":incoming_envelope:"),
    ARROWS_COUNTERCLOCKWISE(":arrows_counterclockwise:"),
    NEW(":new:"),
    CITY(":cityscape:"),
    MAG(":mag:"),
    ID(":id:"),
    DATE(":date:"),
    STOPWATCH(":stopwatch:"),
    WHITE_CHECK_MARK(":white_check_mark:"),
    GALLERY(":gallery:"),
    OFFICE(":office:"),
    CALENDAR(":calendar:"),
    PAGE_WITH_CURL(":page_with_curl:"),
    PENCIL(":memo:"),
    HOUSE_BUILDINGS(":house_buildings:"),
    ARROW_LEFT(":arrow_left:"),
    ARROW_RIGHT(":arrow_right:"),
    E_MAIL(":e-mail:"),
    NOTEBOOK(":notebook:"),
    SHRUG(":female_shrug:"),
    SETTINGS(":hammer_and_wrench:"),
    THUMBSUP(":thumbsup:"),
    QUESTION(":question:"),
    NOT(":x:");


    private final String value;

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

    Icon(String value) {
        this.value = value;
    }
}
