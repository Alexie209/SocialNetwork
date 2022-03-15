package com.example.proiectextinssac.domain.validators;

import com.example.proiectextinssac.domain.User;


public class UserValidator implements Validator<User> {

    public UserValidator() {
    }

    @Override
    public void validate(User entity) throws ValidationException {
        String message = "";
        if (entity.getFirstName().length() == 0) {
            message += "First name can't be an empty string!";
        }
        if (entity.getLastName().length() == 0) {
            message += "Last name can't be an empty string!";
        }
        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}
