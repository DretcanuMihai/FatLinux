package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.AccountStatus;
import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.model.entities_dto.UserServiceDTO;
import com.map_toysocialnetworkgui.model.validators.UserValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.with_db.UserDBRepository;

import java.time.LocalDate;
import java.util.Collection;

/**
 * a class that incorporates a service that works with user administration
 */
public class UserService {
    /**
     * associated users repo
     */
    private final UserDBRepository usersRepo;
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
    public UserService(UserDBRepository usersRepo, UserValidator userValidator) {
        this.usersRepo = usersRepo;
        this.userValidator = userValidator;
    }

    /**
     * adds a user to the repo
     *
     * @param dto - dto containing needed information
     * @throws ValidationException - if the user data is invalid
     * @throws AdministrationException       - if the email is already in use
     */
    public void createUserAccount(UserServiceDTO dto)
            throws ValidationException, AdministrationException {

        User user = new User(dto.getEmail(), dto.getPasswordHash(),dto.getFirstName(),dto.getLastName(),LocalDate.now());
        userValidator.validateDefault(user);
        usersRepo.save(user);
    }

    /**
     * returns a user identified by an email
     *
     * @param email -  said user's email
     * @return - said user
     * @throws ValidationException     - if the email is invalid
     * @throws com.map_toysocialnetworkgui.service.AdministrationException - if a user with said email doesn't exist
     */
    public User getUserInfo(String email) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        userValidator.validateEmail(email);
        User user = usersRepo.get(email);
        if (user == null)
            throw new com.map_toysocialnetworkgui.service.AdministrationException("No user with such email!\n");
        return user;
    }

    /**
     * updates the data of a user identified by an email
     *
     * @param dto - needed data
     * @throws ValidationException     - if any of the data is invalid
     * @throws com.map_toysocialnetworkgui.service.AdministrationException - if a user with said email doesn't exist
     */
    public void updateUserAccountInfo(UserServiceDTO dto)
            throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {

        User user = new User(dto.getEmail(), dto.getPasswordHash(),dto.getFirstName(),dto.getLastName(),null);
        userValidator.validateDefault(user);
        usersRepo.updateInfo(user);
    }

    /**
     * disables a user identified by an email
     *
     * @param email - said user's email
     * @throws ValidationException     - if said email is invalid
     * @throws com.map_toysocialnetworkgui.service.AdministrationException - if a user with the specified email address doesn't exist
     */
    public void disableUserAccount(String email) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        changeUserAccountStatus(email, AccountStatus.DISABLED);
    }

    /**
     * reactivates a user identified by an email
     *
     * @param email - said user's email
     * @throws ValidationException     - if said email is invalid
     * @throws com.map_toysocialnetworkgui.service.AdministrationException - if a user with the specified email address doesn't exist
     */
    public void reactivateUserAccount(String email) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        changeUserAccountStatus(email, AccountStatus.ACTIVE);
    }

    /**
     * changes a user's account status (must be valid)
     *
     * @param email  - said user's email
     * @param status - the new status
     * @throws ValidationException     - if said email is invalid
     * @throws com.map_toysocialnetworkgui.service.AdministrationException - if a user with the specified email address doesn't exist
     */
    private void changeUserAccountStatus(String email, AccountStatus status) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        userValidator.validateEmail(email);
        usersRepo.updateStatus(email, status);
    }

    /**
     * returns a collection of all the users in repo
     *
     * @return said collection
     */
    public Collection<User> getAllUsers() {
        return usersRepo.getAll();
    }

    /**
     * verifies if a list of emails contains only emails of existing users
     *
     * @param emailList - said email list
     * @throws ValidationException     - if any id is invalid
     * @throws com.map_toysocialnetworkgui.service.AdministrationException - if any email doesn't belong to a user
     */
    public void verifyEmailCollection(Collection<String> emailList) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        if(emailList==null)
            throw new ValidationException("Error: email list must be non null!;\n");
        emailList.forEach(this::getUserInfo);
    }

    /**
     * verifies if user email and password hash exists for logging in
     *
     * @param userEmail    - said user's email
     * @param userPassword - said user's password hash
     * @throws ValidationException     - if said user's email is invalid
     * @throws com.map_toysocialnetworkgui.service.AdministrationException - if credentials are invalid
     * @return the user's info
     */
    public User login(String userEmail, int userPassword) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        userValidator.validateEmail(userEmail);
        User found = usersRepo.get(userEmail);

        if (found == null || found.getPasswordHash() != userPassword)
            throw new com.map_toysocialnetworkgui.service.AdministrationException("Invalid email or password!\n");
        return found;
    }
}
