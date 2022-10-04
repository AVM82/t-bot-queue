package ua.shpp.eqbot.validation;

public interface UserValidateService {

    /**
     * Checks whether the new user can be created.
     * @param name - name user
     * @param phone - phone user
     */
    void checkUserCreation(String name, String phone);
}