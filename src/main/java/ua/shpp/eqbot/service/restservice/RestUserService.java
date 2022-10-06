package ua.shpp.eqbot.service.restservice;

import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.dto.UserRestDto;
import ua.shpp.eqbot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class    RestUserService {

    final UserRepository repository;

    public RestUserService(UserRepository repository) {
        this.repository = repository;
    }


    public List<UserEntity> getAllUsers() {
        return repository.findAll();
    }

    public UserEntity getUser(Long telegramId) {
        return repository.findByTelegramId(telegramId);
    }

    public void postUser(UserRestDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setTelegramId(userDto.getTelegramId());
        userEntity.setCity(userDto.getCity());
        userEntity.setLanguage(userDto.getLanguage());
        userEntity.setName(userDto.getName());
        userEntity.setPhone(userDto.getPhone());
        userEntity.setCreatedTime(LocalDateTime.now());
        repository.save(userEntity);
    }

    public void deleteUser(Long telegramId) {
        repository.deleteById(telegramId);
    }
}
