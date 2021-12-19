package com.map_toysocialnetworkgui.model.validators;

import com.map_toysocialnetworkgui.model.entities.User;

import java.util.regex.Pattern;

/**
 * user entity validator
 */
public class UserValidator implements Validator<User>{
    static private final Pattern emailPattern =Pattern.compile("^[^@ ]+@[^@ ]+$");
    static private final int maxSize=50;

    /**
     * validates the user entity if:
     * ->its email is non-null, non-empty, doesn't have commas and matches the email pattern
     * ->its first name and last name are non-null, non-empty and don't have commas
     * ->its passwordHash is not 0
     * @param entity - said entity
     * @throws ValidationException - if said entity is not valid
     */
    @Override
    public void validateD(User entity) throws ValidationException {
        String message="";
        String email=entity.getEmail();
        String firstName=entity.getFirstName();
        String lastName = entity.getLastName();
        int passwordHash=entity.getPasswordHash();
        if(email==null || email.equals("")|| !emailPattern.matcher(email).matches()
                || email.length()>maxSize)
            message+="Invalid email! Must be non empty, under 50 characters and" +
                    " be a valid email address;\n";
        if(firstName==null ||firstName.equals("") || firstName.length()>maxSize)
            message += "Invalid first name! Must be non empty and under 50 characters;\n";
        if(lastName==null || lastName.equals("") || lastName.length() > maxSize)
            message += "Invalid last name! Must be non empty and under 50 characters;\n";
        if(passwordHash==0)
            message+="Invalid password! Must be non empty;\n";
        if(!message.equals("")) {
            message="Error:\n"+message;
            throw new ValidationException(message);
        }
    }

    public void validateEmail(String email)throws ValidationException {
        if(email==null || email.equals("")|| !emailPattern.matcher(email).matches()
                || email.length()>maxSize)
            throw new ValidationException("""
                    Error:
                    Invalid email! Must be non empty, under 50 characters and be a valid email address;
                    """);
    }
}
