package com.map_toysocialnetworkgui.model.entities_dto;

public class UserServiceDTO {
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
     * password hash info
     */
    private final int passwordHash;

    /**
     * creates a dto with given information
     *
     * @param email        - user's email
     * @param firstName    - user's first name
     * @param lastName     - user's last name
     * @param passwordHash - user's password hash
     */
    public UserServiceDTO(String email, String firstName, String lastName, int passwordHash) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
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
     * gets the user's password hash
     *
     * @return said hash
     */
    public int getPasswordHash() {
        return passwordHash;
    }
}
