package ua.shpp.eqbot.cache;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.entity.UserDto;

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
        if (userDto.getId() != null) {
            users.put(userDto.getId(), userDto);
        }
    }

    @Override
    public void remove(UserDto userDto) {
        users.remove(userDto.getId());
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
