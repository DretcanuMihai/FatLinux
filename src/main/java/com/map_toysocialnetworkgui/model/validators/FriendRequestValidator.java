package com.map_toysocialnetworkgui.model.validators;

import com.map_toysocialnetworkgui.model.entities.FriendRequest;

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
        if (!message.equals(""))
            throw new ValidationException(message);
    }

    public void validateEmails(String email1, String email2) throws ValidationException {
        if (Objects.equals(email1, email2))
            throw new ValidationException("A friendship request between an user and himself can't exist!\n");
    }
}
