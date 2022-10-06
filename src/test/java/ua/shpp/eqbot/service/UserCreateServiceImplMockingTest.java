package ua.shpp.eqbot.service;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Example;
import ua.shpp.eqbot.ecxeption.ValidationFailedException;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.validation.UserValidateService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 */
@DataJpaTest
@DisplayName("UserCreateServiceImpl: tests with mocks")
public class UserCreateServiceImplMockingTest {

    private final UserValidateService userValidateService = mock(UserValidateService.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CacheManager cacheManager = mock(CacheManager.class);

    private final UserService userService = new UserService(userRepository, userValidateService);

    @Test
    @DisplayName("Should fail user creation if validation does not pass")
    void shouldFailUserCreation() {
        final var name = "Jack";
        final var phone = "123";
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name).setPhone(phone);
        doThrow(new ValidationFailedException(""))
                .when(userValidateService)
                .checkUserCreation(name, phone);
        assertThrows(ValidationFailedException.class, () -> userService.saveEntity(userEntity));
    }

    @Test
    @DisplayName("Should create new user successfully")
    void shouldCreateNewUser() {
        final var name = "Oleksandr";
        final var phone = "777";
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name).setPhone(phone);

        when(userRepository.save(any()))
                .thenAnswer(invocation -> {
                    UserEntity user = invocation.getArgument(0);
                    assert Objects.equals(user.getName(), name);
                    assert Objects.equals(user.getPhone(), phone);
                    return user.setTelegramId(1L);
                });
    }
//    @Test
//    @Disabled
//    void shouldRollbackIfAnyUserIsNotValidated() {
//        final var name = "Oleksandr";
//        final var phone = "777";
//        UserEntity userEntity = new UserEntity();
//        userEntity.setName(name).setPhone(phone);
//        userService.saveEntity(userEntity);
//        doThrow(new ValidationFailedException(""))
//                .when(userValidateService)
//                .checkUserCreation("Oleksandr", "777 ");
//
//        assertThrows(ValidationFailedException.class, () -> userService.saveEntity(userEntity));
//
//        assertEquals(0, userRepository.count());
//    }
}
