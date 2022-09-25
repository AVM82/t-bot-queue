package ua.shpp.eqbot.hadlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ua.shpp.eqbot.model.PositionMenu;
import ua.shpp.eqbot.model.UserDto;

import java.util.HashMap;
import java.util.Map;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery> {
    private final Logger log = LoggerFactory.getLogger(CallbackQueryHandler.class);
    private final Map<Long, UserDto> cache = new HashMap<>();

    public CallbackQueryHandler() {
    }

    @Override
    public void choose(CallbackQuery callbackQuery) {
        UserDto user = cache.get(callbackQuery.getMessage().getChatId());
        if (callbackQuery.getData().equals("create_service")) {
            log.info("create_service");
            user.setPositionMenu(PositionMenu.MENU_CREATE_SERVICE);
        }
        if (callbackQuery.getData().equals("search_service")) {
            log.info("search_service");
            user.setPositionMenu(PositionMenu.MENU_SEARCH_SERVICE);
        }
    }

    public Map<Long, UserDto> getCache() {
        return cache;
    }
}
