package ua.shpp.eqbot.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.shpp.eqbot.ecxeption.ValidationFailedException;
import ua.shpp.eqbot.repository.UserRepository;

@Service
@RequiredArgsConstructor
class UserValidateServiceImpl implements UserValidateService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public void checkUserCreation(String firstName, String lastName) {
        if (userRepository.existsByNameAndPhone(firstName, lastName)) {
            throw new ValidationFailedException("Such person already exists");
        }
    }
}