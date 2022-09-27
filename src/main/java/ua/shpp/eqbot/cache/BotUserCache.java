package ua.shpp.eqbot.cache;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.model.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotUserCache{

    private static Map<Long, UserDto> users = new HashMap<>();

    public static void add(UserDto userDto) {
        if (userDto.getId_telegram() != null) {
            users.put(userDto.getId_telegram(), userDto);
        }
    }

    public static void remove(UserDto userDto) {
        users.remove(userDto.getId_telegram());
    }

    public static UserDto findBy(Long id) {
        return users.get(id);
    }

    public static List<UserDto> getAll() {
        return new ArrayList<>(users.values());
    }
}
