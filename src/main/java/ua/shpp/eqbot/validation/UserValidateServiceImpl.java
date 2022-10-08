package ua.shpp.eqbot.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.shpp.eqbot.repository.UserRepository;

@Service
public class UserValidateServiceImpl implements UserValidateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidateServiceImpl.class);

    private final UserRepository userRepository;

    public UserValidateServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkUserCreation(String name, String phone) {
        if (userRepository.existsByNameAndPhone(name, phone)) {
            LOGGER.error("Such person already exists");
            return false;
        }
        return true;
    }
}