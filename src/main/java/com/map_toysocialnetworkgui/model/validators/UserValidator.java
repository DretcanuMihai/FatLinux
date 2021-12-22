package com.map_toysocialnetworkgui.model.validators;

import com.map_toysocialnetworkgui.model.entities.User;

import java.util.regex.Pattern;

/**
 * user entity validator
 */
public class UserValidator implements Validator<User> {
    static private final Pattern emailPattern = Pattern.compile("^[^@ ]+@[^@ ]+$");
    static private final int maxSize = 50;

    /**
     * validates the user entity if:
     * ->its email is non-null, non-empty but under 50 characters, and is a valid email
     * ->its first name and last name are non-null, non-empty and under 50 characters
     *
     * @param entity - said entity
     * @throws ValidationException - if said entity is not valid
     */
    @Override
    public void validateDefault(User entity) throws ValidationException {
        String message = "";
        String email = entity.getEmail();
        String firstName = entity.getFirstName();
        String lastName = entity.getLastName();
        if (email == null || email.equals("") || !emailPattern.matcher(email).matches()
                || email.length() > maxSize)
            message += "Invalid email! Must be non empty, under 50 characters and" +
                    " be a valid email address;\n";
        if (firstName == null || firstName.equals("") || firstName.length() > maxSize)
            message += "Invalid first name! Must be non empty and under 50 characters;\n";
        if (lastName == null || lastName.equals("") || lastName.length() > maxSize)
            message += "Invalid last name! Must be non empty and under 50 characters;\n";
        if (!message.equals("")) {
            message = "Error:\n" + message;
            throw new ValidationException(message);
        }
    }

    /**
     * validates an email if said email is non-null, non-empty but under 50 characters, and is a valid email
     *
     * @param email - said email
     * @throws ValidationException - if said email is not valid
     */
    public void validateEmail(String email) throws ValidationException {
        if (email == null || email.equals("") || !emailPattern.matcher(email).matches()
                || email.length() > maxSize)
            throw new ValidationException("""
                    Error:
                    Invalid email! Must be non empty,
                    under 50 characters
                    and be a valid email address;
                    """);
    }
}
