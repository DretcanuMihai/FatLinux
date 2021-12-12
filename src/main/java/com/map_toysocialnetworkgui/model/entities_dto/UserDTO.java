package com.map_toysocialnetworkgui.model.entities_dto;

import com.map_toysocialnetworkgui.model.entities.User;

import java.time.LocalDate;

/**
 * DTO class for User entity
 */
public class UserDTO {
    /**
     * email info
     */
    private final String email;

    /**
     * first name info
     */
    private final String firstName;

    /**
     * last name info
     */
    private final String lastName;

    /**
     * join date info
     */
    private final LocalDate joinDate;

    /**
     * creates a user DTO for a user
     *
     * @param user - said user
     */
    public UserDTO(User user) {
        email = user.getEmail();
        firstName = user.getFirstName();
        joinDate = user.getJoinDate();
        lastName = user.getLastName();
    }

    /**
     * gets user's email
     *
     * @return said email
     */
    public String getEmail() {
        return email;
    }

    /**
     * gets the user's first name
     *
     * @return said first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * gets the user's last name
     *
     * @return said last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * gets the date the user joined
     *
     * @return said date
     */
    public LocalDate getJoinDate() {
        return joinDate;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", joinDate=" + joinDate +
                '}';
    }
}
