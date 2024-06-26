package com.fatlinux.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * class that defines user entity
 */
public class User extends Entity<String> {
    /**
     * associated join date
     */
    private final LocalDate joinDate;

    /**
     * associated first name
     */
    private String firstName;

    /**
     * associated last name
     */
    private String lastName;

    /**
     * associated passwordHash
     */
    private String passwordHash;

    /**
     * asociated last time the user logged in
     */
    private LocalDateTime lastLoginTime;

    /**
     * creates a user based on the given information
     *
     * @param email        - user's email
     * @param passwordHash - user's password hash
     * @param firstName    - user's first name
     * @param lastName     - user's last name
     * @param joinDate     - the date the user joined
     * @param lastLoginTime - the last time the user logged in
     */
    public User(String email, String passwordHash, String firstName, String lastName, LocalDate joinDate,LocalDateTime lastLoginTime) {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.joinDate = joinDate;
        this.lastLoginTime=lastLoginTime;
    }

    /**
     * creates a user based on the given information
     *
     * @param email        - user's email
     * @param passwordHash - user's password hash
     * @param firstName    - user's first name
     * @param lastName     - user's last name
     * @param joinDate     - the date the user joined
     */
    public User(String email, String passwordHash, String firstName, String lastName, LocalDate joinDate) {
        this(email,passwordHash,firstName,lastName,joinDate,LocalDateTime.now());
    }

    /**
     * gets the user's email
     *
     * @return said email
     */
    public String getEmail() {
        return getId();
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
     * sets the user's first name
     *
     * @param firstName - new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
     * set the user's last name
     *
     * @param lastName - new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * gets user's password's hash
     *
     * @return said hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * sets the user's password's hash
     *
     * @param passwordHash - new password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * gets the date the user joined
     *
     * @return said date
     */
    public LocalDate getJoinDate() {
        return joinDate;
    }

    /**
     * gets the time the user last logged in
     *
     * @return said time
     */
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * sets the last time the user logged in
     * @param lastLoginTime - the new time
     */
    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * verifies if instance is equal to an object
     * instance is equal with object if they are of same type and have the same attributes
     *
     * @param o - the object with which we verify equality
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof User user))
            return false;
        return Objects.equals(getEmail(), user.getEmail()) && Objects.equals(firstName, user.firstName)
                && Objects.equals(passwordHash, user.passwordHash) && Objects.equals(joinDate, user.joinDate);
    }

    /**
     * calculates the hashcode of the instance based on all the attributes
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), firstName, passwordHash, joinDate);
    }
}
