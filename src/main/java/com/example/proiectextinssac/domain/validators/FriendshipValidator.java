package com.example.proiectextinssac.domain.validators;


import com.example.proiectextinssac.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    public FriendshipValidator() {
    }

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String message = "";

        if (entity.getId1().equals(entity.getId2())) {
            message += "User id's can't be equal!";
        }
        if (entity.getId1() <= 0 || entity.getId2() <= 0) {
            message += "User id's can't be negative!";
        }

        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}