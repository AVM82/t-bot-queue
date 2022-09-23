package ua.shpp.eqbot.service;

import org.springframework.stereotype.Service;
import ua.shpp.eqbot.entity.UserEntity;

import java.util.HashMap;

@Service
public class RegistrationUser {
    HashMap<Integer, UserEntity> users;

    public RegistrationUser() {
        users = new HashMap<>();
    }

    private boolean changeregistrationUser(Long telegramId){
        return users.get(telegramId) == null;
    }

    private boolean registration (){
        return true;
    }
}
