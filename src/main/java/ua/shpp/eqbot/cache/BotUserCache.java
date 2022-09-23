package ua.shpp.eqbot.cache;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.entity.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotUserCache implements Cache<UserEntity> {

    private final Map<Long,UserEntity> users;

    public BotUserCache() {
        this.users = new HashMap<>();
    }

    @Override
    public void add(UserEntity userEntity) {
        if(userEntity.getId() != null){
            users.put(userEntity.getId(),userEntity);
        }
    }

    @Override
    public void remove(UserEntity userEntity) {
        users.remove(userEntity.getId());
    }

    @Override
    public UserEntity findBy(Long id) {
        return users.get(id);
    }

    @Override
    public List<UserEntity> getAll() {
        return new ArrayList<>(users.values());
    }
}
