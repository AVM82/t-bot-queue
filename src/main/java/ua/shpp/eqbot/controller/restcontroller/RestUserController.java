package ua.shpp.eqbot.controller.restcontroller;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.model.UserRestDto;
import ua.shpp.eqbot.service.restservice.RestUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class RestUserController {

    Logger log = LoggerFactory.getLogger(RestUserController.class);
    @Autowired
    RestUserService service;

    @GetMapping
    @Operation(summary = "${operation.summary.user.get_all_users}",
            description = "${operation.desc.user.get_all_users}")
    public List<UserEntity> getAllUsers(){
        log.info("Getting all users");
        return service.getAllUsers();
    }

    @GetMapping("/{idTelegram}")
    @Operation(summary = "${operation.summary.user.get_user}",
            description = "${operation.desc.user.get_user}")
    public UserEntity getUser(@PathVariable Long idTelegram){
        log.info("Getting user with Telegram {}", idTelegram);
        return service.getUser(idTelegram);
    }

    @PostMapping
    @Operation(summary = "${operation.summary.user.post_user}",
            description = "${operation.desc.user.post_user}")
    public void postUser(@RequestBody @Valid UserRestDto userDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                log.error(error.getDefaultMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entered data is not valid" );
        } else {
            service.postUser(userDto);
            log.info("Adding new service with name '{}'", userDto.getName());
        }

    }

    @DeleteMapping("/{idTelegram}")
    @Operation(summary = "${operation.summary.user.delete_user}",
            description = "${operation.desc.user.delete_user}")
    public void deleteUser(@PathVariable Long idTelegram){
        log.info("Deleting user with Telegram id {}", idTelegram);
        service.deleteUser(idTelegram);
    }




}
