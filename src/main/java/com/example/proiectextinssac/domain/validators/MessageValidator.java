package com.example.proiectextinssac.domain.validators;


import com.example.proiectextinssac.domain.Message;

public class MessageValidator implements Validator<Message> {

    public MessageValidator() {
    }

    @Override
    public void validate(Message entity) throws ValidationException {
        String message = "";

        if (entity.getTo().equals(entity.getFrom())) {
            message += "User id's can't be equal!";
        }
        if (entity.getTo() <= 0L || entity.getFrom() <= 0L) {
            message += "User id's can't be negative!";
        }
        if (entity.getMessage().length() == 0) {
            message += "Message can't be an empty string!";
        }

        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}

