package com.example.proiectextinssac.domain.validators;


import com.example.proiectextinssac.domain.FriendRequest;

public class FriendRequestValidator implements Validator<FriendRequest> {

    public FriendRequestValidator() {
    }

    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String message = "";

        if (entity.getId1().equals(entity.getId2())) {
            message += "User id's can't be equal!";
        }
        if (entity.getId1() <= 0L || entity.getId2() <= 0L) {
            message += "User id's can't be negative!";
        }

        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}
