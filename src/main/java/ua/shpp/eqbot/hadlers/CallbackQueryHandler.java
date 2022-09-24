package ua.shpp.eqbot.hadlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ua.shpp.eqbot.cache.Cache;
import ua.shpp.eqbot.entity.PositionMenu;
import ua.shpp.eqbot.entity.UserDto;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery>{
    Logger log = LoggerFactory.getLogger(CallbackQueryHandler.class);
    private final Cache<UserDto> cache;

    public CallbackQueryHandler(Cache<UserDto> cache) {
        this.cache = cache;
    }

    @Override
    public void choose(CallbackQuery callbackQuery) {
        UserDto user = cache.findBy(callbackQuery.getMessage().getChatId());
        if(callbackQuery.getData().equals("create_service")) {
            log.info("create_service");
            user.setPositionMenu(PositionMenu.MENU_CREATE_SERVICE);
        }
        if(callbackQuery.getData().equals("search_service")) {
            log.info("search_service");
            user.setPositionMenu(PositionMenu.MENU_SEARCH_SERVICE);
        }
    }
}
