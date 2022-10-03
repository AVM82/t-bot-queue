package ua.shpp.eqbot.service.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.model.UserRestDto;
import ua.shpp.eqbot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestUserService {

    @Autowired
    UserRepository repository;


    public List<UserEntity> getAllUsers(){
        return repository.findAll();
    }

    public UserEntity getUser(Long idTelegram){
        return repository.findFirstByIdTelegram(idTelegram);
    }

    public void postUser(UserRestDto userDto){
        UserEntity userEntity = new UserEntity();
        userEntity.setIdTelegram(userDto.getIdTelegram());
        userEntity.setCity(userDto.getCity());
        userEntity.setLanguage(userDto.getLanguage());
        userEntity.setName(userDto.getName());
        userEntity.setPhone(userDto.getPhone());
        userEntity.setTimeCreated(LocalDateTime.now());
        repository.save(userEntity);
    }

    public void deleteUser(Long idTelegram){
        repository.deleteById(idTelegram);
    }
}