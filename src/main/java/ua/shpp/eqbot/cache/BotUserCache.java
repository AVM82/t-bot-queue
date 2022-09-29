package ua.shpp.eqbot.cache;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.model.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotUserCache {

    private static final Map<Long, UserDto> users = new HashMap<>();

    private BotUserCache() {
    }

    public static void add(UserDto userDto) {
        if (userDto.getIdTelegram() != null) {
            users.put(userDto.getIdTelegram(), userDto);
        }
    }

    public static void remove(UserDto userDto) {
        users.remove(userDto.getIdTelegram());
    }

    public static UserDto findBy(Long id) {
        return users.get(id);
    }

    public static List<UserDto> getAll() {
        return new ArrayList<>(users.values());
    }
}
