package com.example.map_toysocialnetworkgui.service;

import com.example.map_toysocialnetworkgui.model.entities.User;
import com.example.map_toysocialnetworkgui.model.validators.UserValidator;
import com.example.map_toysocialnetworkgui.model.validators.ValidationException;
import com.example.map_toysocialnetworkgui.repository.Repository;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * a class that incorporates a service that works with user administration
 */
public class UserService {
    /**
     * associated users repo
     */
    private final Repository<String, User> usersRepo;
    /**
     * associated user validator
     */
    private final UserValidator userValidator;

    /**
     * constructs an userService having a specified user repo and validator
     * @param usersRepo - said user Repo
     * @param userValidator - said user validator
     */
    public UserService(Repository<String, User> usersRepo, UserValidator userValidator) {
        this.usersRepo = usersRepo;
        this.userValidator = userValidator;
    }

    /**
     * adds a user to the repo
     * @param email - said user's email
     * @param firstName - said user's first name
     * @param passwordHash - said user's password's hash
     * @param lastName - said user's last name
     * @throws ValidationException - if the user data is invalid
     * @throws AdministrationException - if a user with the same email already exists
     */
    public void addUser(String email, String firstName, int passwordHash, String lastName)
            throws ValidationException, AdministrationException {

        User user = new User(email, firstName, lastName, passwordHash, LocalDate.now());
        userValidator.validate(user);
        if(usersRepo.get(email) != null)
            throw new AdministrationException("Email already in use!\n");
        usersRepo.save(user);
    }

    /**
     * returns a user identified by an email
     * @param email -  said user's email
     * @return - said user
     * @throws ValidationException - if the email is invalid
     * @throws AdministrationException - if a user with said email doesn't exist
     */
    public User getUser(String email) throws ValidationException, AdministrationException {
        userValidator.validateEmail(email);
        User user = usersRepo.get(email);
        if(user == null)
            throw new AdministrationException("No user with such email!\n");
        return user;
    }

    /**
     * updates the data of a user identified by an email
     * @param email - said user's email
     * @param firstName - the new first name
     * @param passwordHash -  the new password hash
     * @param lastName - the new last name
     * @throws ValidationException - if any of the data is invalid
     * @throws AdministrationException - if a user with said email doesn't exist
     */
    public void updateUser(String email, String firstName, int passwordHash, String lastName)
            throws ValidationException, AdministrationException {

        User user = new User(email, firstName, lastName, passwordHash, LocalDate.now());
        userValidator.validate(user);
        User oldUser = usersRepo.get(email);
        if(oldUser == null)
            throw new AdministrationException("No user with such email!\n");
        usersRepo.update(new User(email,firstName, lastName, passwordHash,oldUser.getJoinDate()));
    }

    /**
     * deletes a user identified by an email
     * @param email - said user's email
     * @throws ValidationException - if said email is invalid
     * @throws AdministrationException - if a user with the specified email address doesn't exist
     */
    public void deleteUser(String email) throws ValidationException,AdministrationException {
        userValidator.validateEmail(email);
        if(usersRepo.get(email) == null)
            throw new AdministrationException("No user with such email!\n");
        usersRepo.delete(email);
    }

    /**
     * returns an iterable of all the users in repo
     * @return said iterable
     */
    public Collection<User> getAllUsers() {
        return usersRepo.getAll();
    }

    /**
     * verifies if a list of emails contains only emails of existing users
     * @param emailList - said email list
     * @throws ValidationException - if any id is invalid
     * @throws AdministrationException - if any email doesn't belong to a user
     */
    public void verifyEmailList(List<String> emailList) throws ValidationException, AdministrationException {
        if(emailList == null)
            throw new ValidationException("Error: emailList must be non null;\n");
        emailList.forEach(this::getUser);
    }

    /**
     * verifies if user email and password hash exists for logging in
     * @param userEmail - said user's email
     * @param userPassword - said user's password hash
     * @throws ValidationException - if said user's email is invalid
     * @throws AdministrationException - if credentials are invalid
     */
    public void userLogin(String userEmail, String userPassword) throws ValidationException, AdministrationException {
        userValidator.validateEmail(userEmail);
        User found = usersRepo.get(userEmail);

        if (found == null || found.getPasswordHash() != userPassword.hashCode())
            throw new AdministrationException("Invalid email or password!\n");
    }
}
