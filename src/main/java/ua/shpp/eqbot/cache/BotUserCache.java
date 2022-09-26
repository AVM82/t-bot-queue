package ua.shpp.eqbot.cache;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.model.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotUserCache implements Cache<UserDto> {

    private final Map<Long, UserDto> users;

    public BotUserCache() {
        this.users = new HashMap<>();
    }

    @Override
    public void add(UserDto userDto) {
        if (userDto.getId_telegram() != null) {
            users.put(userDto.getId_telegram(), userDto);
        }
    }

    @Override
    public void remove(UserDto userDto) {
        users.remove(userDto.getId_telegram());
    }

    @Override
    public UserDto findBy(Long id) {
        return users.get(id);
    }

    @Override
    public List<UserDto> getAll() {
        return new ArrayList<>(users.values());
    }
}
