package com.fatlinux.model.validators;

import com.fatlinux.model.entities.FriendRequest;

import java.util.Objects;

/**
 * friend request entity validator
 */
public class FriendRequestValidator implements Validator<FriendRequest> {
    @Override
    public void validateDefault(FriendRequest entity) throws ValidationException {
        String message = "";
        String email1 = entity.getSender();
        String email2 = entity.getReceiver();
        if (Objects.equals(email1, email2))
            message += "A friendship request between an user and himself can't exist!\n";
        if (!message.equals("")) {
            message = "Error:\n" + message;
            throw new ValidationException(message);
        }
    }

    public void validateEmails(String email1, String email2) throws ValidationException {
        if (Objects.equals(email1, email2))
            throw new ValidationException("Error:\nA friendship request between an user and himself can't exist!\n");
    }
}
