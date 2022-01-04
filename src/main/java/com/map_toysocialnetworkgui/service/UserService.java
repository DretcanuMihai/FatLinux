package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.model.validators.UserValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.UserRepositoryInterface;
import com.map_toysocialnetworkgui.utils.events.ChangeEventType;
import com.map_toysocialnetworkgui.utils.events.EntityModificationEvent;
import com.map_toysocialnetworkgui.utils.observer.AbstractObservable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * class that incorporates a service that works with user administration
 */
public class UserService extends AbstractObservable<EntityModificationEvent<String>> {
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
     * @param usersRepo     - said user Repo
     * @param userValidator - said user validator
     */
    public UserService(UserRepositoryInterface usersRepo, UserValidator userValidator) {
        this.usersRepo = usersRepo;
        this.userValidator = userValidator;
    }

    /**
     * creates a user account
     *
     * @param email             - email info
     * @param encryptedPassword - encrypted password info
     * @param firstName         - first name info
     * @param lastName          - last name info
     * @throws ValidationException     - if the user data is invalid
     * @throws AdministrationException - if the email is already in use
     */
    public void createUserAccount(String email, String encryptedPassword, String firstName, String lastName)
            throws ValidationException, AdministrationException {

        User user = new User(email, encryptedPassword, firstName, lastName, LocalDate.now());
        userValidator.validateDefault(user);
        User result = usersRepo.save(user);
        if (result != null)
            throw new AdministrationException("Error: email already in use;\n");
        notifyObservers(new EntityModificationEvent<>(ChangeEventType.ADD, email));
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
     * @param email             - email info
     * @param encryptedPassword - encrypted password info
     * @param firstName         - first name info
     * @param lastName          - last name info
     * @throws ValidationException     - if any of the data is invalid
     * @throws AdministrationException - if a user with said email doesn't exist
     */
    public void updateUserAccountInfo(String email, String encryptedPassword, String firstName, String lastName)
            throws ValidationException, AdministrationException {

        User user = new User(email, encryptedPassword, firstName, lastName, null);
        userValidator.validateDefault(user);
        User actualUser = usersRepo.findOne(email);
        if (actualUser == null)
            throw new AdministrationException("Error: email not in use;\n");
        actualUser.setFirstName(firstName);
        actualUser.setLastName(lastName);
        actualUser.setEncryptedPassword(encryptedPassword);
        usersRepo.update(user);
        notifyObservers(new EntityModificationEvent<>(ChangeEventType.UPDATE, email));
    }

    /**
     * deletes a user identified by an email
     *
     * @param email - said user's email
     * @throws ValidationException     - if said email is invalid
     * @throws AdministrationException - if a user with the specified email address doesn't exist
     */
    public void deleteUserAccount(String email) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        userValidator.validateEmail(email);
        User result = usersRepo.delete(email);
        if (result == null)
            throw new AdministrationException("Error: no user with given email;\n");
        notifyObservers(new EntityModificationEvent<>(ChangeEventType.DELETE, email));
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
     * verifies if user email and password hash exists for logging in
     *
     * @param userEmail         - said email
     * @param encryptedPassword - said encrypted password
     * @return the user's info
     * @throws ValidationException     - if said user's email is invalid
     * @throws AdministrationException - if credentials are invalid
     */
    public User login(String userEmail, String encryptedPassword) throws ValidationException, AdministrationException {
        userValidator.validateEmail(userEmail);
        User found = usersRepo.findOne(userEmail);

        if (found == null || !found.getEncryptedPassword().equals(encryptedPassword))
            throw new AdministrationException("Invalid email or password!\n");
        return found;
    }

    /**
     * returns an iterable of all the users in repo with certain string inside of them
     *
     * @param string - sais string
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
}
