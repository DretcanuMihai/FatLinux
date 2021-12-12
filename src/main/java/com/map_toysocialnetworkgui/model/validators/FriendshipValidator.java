package com.map_toysocialnetworkgui.model.validators;

import com.map_toysocialnetworkgui.model.entities.Friendship;

import java.util.Objects;

/**
 * friendship entity validator
 */
public class FriendshipValidator implements Validator<Friendship> {

    /**
     * validates the friendship entity if:
     * ->its emails are not equal
     * @param entity - said entity
     * @throws ValidationException - if said entity is not valid
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String message="";
        String email1=entity.getEmails().getFirst();
        String email2=entity.getEmails().getSecond();
        if(Objects.equals(email1,email2))
            message+="A friendship between an user and himself can't exist!\n";
        if(!message.equals(""))
            throw new ValidationException(message);
    }

    public void validateEmails(String email1,String email2) throws ValidationException {
        if(Objects.equals(email1,email2))
            throw new ValidationException("A friendship between an user and himself can't exist!\n");
    }
}
