package com.fatlinux.service;

import com.fatlinux.model.entities.User;
import com.fatlinux.model.validators.UserValidator;
import com.fatlinux.model.validators.ValidationException;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.skeletons.entity_based.UserRepositoryInterface;
import com.fatlinux.utils.events.ChangeEventType;
import com.fatlinux.utils.events.EntityModificationObsEvent;
import com.fatlinux.utils.observer.AbstractObservable;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * class that incorporates a service that works with user administration
 */
public class UserService extends AbstractObservable<EntityModificationObsEvent<String>> {
    /**
     * associated users repo
     */

    private final UserRepositoryInterface usersRepo;
    /**
     * associated user validator
     */
    private final UserValidator userValidator;

    /**
     * constructs an userService having a specified user repo and validator
     *
     * @param usersRepo     - said user repo
     * @param userValidator - said user validator
     */
    public UserService(UserRepositoryInterface usersRepo, UserValidator userValidator) {
        this.usersRepo = usersRepo;
        this.userValidator = userValidator;
    }

    /**
     * creates a user account
     *
     * @param email     - email info
     * @param password  - password info
     * @param firstName - first name info
     * @param lastName  - last name info
     * @throws ValidationException     - if the user data is invalid
     * @throws AdministrationException - if the email is already in use
     */
    public void createUserAccount(String email, String password, String firstName, String lastName)
            throws ValidationException, AdministrationException {

        // Hashes the password using argon2i algorithm
        Argon2 argon2 = Argon2Factory.create();
        String passwordHash = argon2.hash(22, 65536, 1, password.toCharArray());
        User user = new User(email, passwordHash, firstName, lastName, LocalDate.now());
        userValidator.validateDefault(user);
        User result = usersRepo.save(user);
        if (result != null)
            throw new AdministrationException("Error: email already in use;\n");
        notifyObservers(new EntityModificationObsEvent<>(ChangeEventType.ADD, email));
    }

    /**
     * returns a user identified by an email
     *
     * @param email -  said user's email
     * @return - said user
     * @throws ValidationException     - if the email is invalid
     * @throws AdministrationException - if a user with said email doesn't exist
     */
    public User getUserInfo(String email) throws ValidationException, AdministrationException {
        userValidator.validateEmail(email);
        User user = usersRepo.findOne(email);
        if (user == null)
            throw new AdministrationException("Error: email not in use;\n");
        return user;
    }

    /**
     * updates the data of a user identified by an email
     *
     * @param email     - email info
     * @param password  - password info
     * @param firstName - first name info
     * @param lastName  - last name info
     * @throws ValidationException     - if any of the data is invalid
     * @throws AdministrationException - if a user with said email doesn't exist
     */
    public void updateUserAccountInfo(String email, String password, String firstName, String lastName)
            throws ValidationException, AdministrationException {

        // Hashes the password using argon2i algorithm
        Argon2 argon2 = Argon2Factory.create();
        String passwordHash = argon2.hash(22, 65536, 1, password.toCharArray());
        User user = new User(email, passwordHash, firstName, lastName, null);
        userValidator.validateDefault(user);
        User actualUser = usersRepo.findOne(email);
        if (actualUser == null)
            throw new AdministrationException("Error: email not in use;\n");
        actualUser.setFirstName(firstName);
        actualUser.setLastName(lastName);
        actualUser.setPasswordHash(passwordHash);
        usersRepo.update(user);
        notifyObservers(new EntityModificationObsEvent<>(ChangeEventType.UPDATE, email));
    }

    /**
     * deletes a user identified by an email
     *
     * @param email - said user's email
     * @throws ValidationException     - if said email is invalid
     * @throws AdministrationException - if a user with the specified email address doesn't exist
     */
    public void deleteUserAccount(String email) throws ValidationException, com.fatlinux.service.AdministrationException {
        userValidator.validateEmail(email);
        User result = usersRepo.delete(email);
        if (result == null)
            throw new AdministrationException("Error: no user with given email;\n");
        notifyObservers(new EntityModificationObsEvent<>(ChangeEventType.DELETE, email));
    }

    /**
     * returns an iterable of all the users in repo
     *
     * @return said iterable
     */
    public Iterable<User> getAllUsers() {
        return usersRepo.findAll();
    }

    /**
     * returns a page of all the users in repo
     *
     * @param pageable - pageable for paging
     * @return said page
     */
    public Page<User> getAllUsers(Pageable pageable) {
        return usersRepo.findAll(pageable);
    }

    /**
     * verifies if a list of emails contains only emails of existing users
     *
     * @param emailList - said email list
     * @throws AdministrationException - if any email is incorrect
     */
    public void verifyEmailCollection(Collection<String> emailList) throws AdministrationException {
        if (emailList == null)
            throw new ValidationException("Error: email list must be non null!;\n");
        List<String> errors = new ArrayList<>();
        emailList.forEach(email -> {
            try {
                getUserInfo(email);
            } catch (AdministrationException | ValidationException e) {
                errors.add("For email:" + email + "\n" + e.getMessage());
            }
        });
        StringBuilder error = new StringBuilder();
        for (String er : errors) {
            error.append(er);
        }
        if (!error.toString().equals(""))
            throw new AdministrationException(error.toString());
    }

    /**
     * verifies if user exists for logging in
     *
     * @param userEmail    - said user's email
     * @param userPassword - said user's password
     * @return the user's info
     * @throws ValidationException     - if said user's email is invalid
     * @throws AdministrationException - if credentials are invalid
     */
    public User login(String userEmail, String userPassword) throws ValidationException, AdministrationException {
        userValidator.validateEmail(userEmail);
        User found = usersRepo.findOne(userEmail);
        Argon2 argon2 = Argon2Factory.create();

        if (found == null || !argon2.verify(found.getPasswordHash(), userPassword.toCharArray()))
            throw new AdministrationException("Invalid email or password!\n");
        else {
            LocalDateTime lastLogin=found.getLastLoginTime();
            found.setLastLoginTime(LocalDateTime.now());
            usersRepo.update(found);
            found.setLastLoginTime(lastLogin);
            return found;
        }
    }

    /**
     * returns an iterable of all the users in repo containing a certain string in their complete names
     *
     * @param string - said string
     * @return said iterable
     * @throws ValidationException if string is null
     */
    public Iterable<User> filterUsers(String string) throws ValidationException {
        if (string == null)
            throw new ValidationException("Error: string must be non null;\n");
        return usersRepo.getUsersByName(string);
    }

    /**
     * returns a page of all the users in repo that have a certain string in their names
     *
     * @param string   - said string
     * @param pageable - pageable for paging
     * @return said page
     * @throws ValidationException if string is null
     */
    public Page<User> filterUsers(String string, Pageable pageable) throws ValidationException {
        if (string == null)
            throw new ValidationException("Error: string must be non null;\n");
        return usersRepo.getUsersByName(string, pageable);
    }

    /**
     * logs a user out
     * @param userEmail - said user's email
     * @throws ValidationException - if any data is invalid
     * @throws AdministrationException - if no user with given email exists
     */
    public void logout(String userEmail) throws ValidationException,AdministrationException{
        User user=getUserInfo(userEmail);
        user.setLastLoginTime(LocalDateTime.now());
        usersRepo.update(user);
    }
}
