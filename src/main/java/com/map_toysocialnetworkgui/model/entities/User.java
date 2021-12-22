package com.map_toysocialnetworkgui.model.entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 * class that defines user entity
 */
public class User extends Entity<String> {
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
    private int passwordHash;

    /**
     * associated join date
     */
    private final LocalDate joinDate;

    /**
     * associated account status
     */
    private AccountStatus accountStatus;

    /**
     * Creates a user based on the given information
     *
     * @param email         - user's email
     * @param passwordHash  - user's password's hashcode
     * @param firstName     - user's first name
     * @param lastName      - user's last name
     * @param joinDate      - the date the user joined
     * @param accountStatus - said user's account status
     */
    public User(String email, int passwordHash, String firstName, String lastName, LocalDate joinDate, AccountStatus accountStatus) {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.joinDate = joinDate;
        this.accountStatus = accountStatus;
    }

    /**
     * Creates a user based on the given information
     *
     * @param email             - user's email
     * @param passwordHash      - user's password's hashcode
     * @param firstName         - user's first name
     * @param lastName          - user's last name
     * @param joinDate          - the date the user joined
     * @param accountStatusCode - said user's account status code
     */
    public User(String email, int passwordHash, String firstName, String lastName, LocalDate joinDate, int accountStatusCode) {
        this(email, passwordHash, firstName, lastName, joinDate, AccountStatus.getByCode(accountStatusCode));
    }

    /**
     * Creates a user based on the given information - the user's account
     * is by default active
     *
     * @param email        - user's email
     * @param passwordHash - user's password's hashcode
     * @param firstName    - user's first name
     * @param lastName     - user's last name
     * @param joinDate     - the date the user joined
     */
    public User(String email, int passwordHash, String firstName, String lastName, LocalDate joinDate) {
        this(email, passwordHash, firstName, lastName, joinDate, AccountStatus.ACTIVE.getStatusCode());
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
    public int getPasswordHash() {
        return passwordHash;
    }

    /**
     * sets the user's password's hash
     *
     * @param passwordHash - new password hash
     */
    public void setPasswordHash(int passwordHash) {
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
     * gets the status of the user's account
     *
     * @return said status
     */
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    /**
     * sets the status of the user's account
     *
     * @param accountStatus - the new status
     */
    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
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
                && Objects.equals(passwordHash, user.passwordHash) && Objects.equals(joinDate, user.joinDate) &&
                Objects.equals(accountStatus, user.accountStatus);
    }

    /**
     * calculates the hashcode of the instance based on all the attributes
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), firstName, passwordHash, joinDate, accountStatus);
    }
}
