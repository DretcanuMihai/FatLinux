package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.AccountStatus;
import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.model.entities_dto.UserServiceDTO;
import com.map_toysocialnetworkgui.model.validators.UserValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.with_db.UserDBRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * creates a user account
     *
     * @param dto - dto containing needed information
     * @throws ValidationException - if the user data is invalid
     * @throws AdministrationException       - if the email is already in use
     */
    public void createUserAccount(UserServiceDTO dto)
            throws ValidationException, AdministrationException {

        User user = new User(dto.getEmail(), dto.getPasswordHash(),dto.getFirstName(),dto.getLastName(),LocalDate.now());
        userValidator.validateDefault(user);
        boolean success=usersRepo.save(user);
        if(!success)
            throw new AdministrationException("Error: email already in use;\n");
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
        User user = usersRepo.get(email);
        if (user == null)
            throw new AdministrationException("Error: email not in use;\n");
        return user;
    }

    /**
     * updates the data of a user identified by an email
     *
     * @param dto - needed data
     * @throws ValidationException     - if any of the data is invalid
     * @throws AdministrationException - if a user with said email doesn't exist
     */
    public void updateUserAccountInfo(UserServiceDTO dto)
            throws ValidationException, AdministrationException {

        User user = new User(dto.getEmail(), dto.getPasswordHash(),dto.getFirstName(),dto.getLastName(),null);
        userValidator.validateDefault(user);
        User actualUser=usersRepo.get(dto.getEmail());
        if(actualUser==null)
            throw new AdministrationException("Error: email not in use;\n");
        if(!actualUser.getAccountStatus().equals(AccountStatus.ACTIVE))
            throw new AdministrationException("Error: account is disabled;\n");
        actualUser.setFirstName(dto.getFirstName());
        actualUser.setLastName(dto.getLastName());
        actualUser.setPasswordHash(dto.getPasswordHash());
        usersRepo.update(user);
    }

    /**
     * disables a user identified by an email
     *
     * @param email - said user's email
     * @throws ValidationException     - if said email is invalid
     * @throws AdministrationException - if a user with the specified email address doesn't exist
     *                                   or if it is already disabled
     */
    public void disableUserAccount(String email) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        changeUserAccountStatus(email,AccountStatus.DISABLED);
    }

    /**
     * reactivates a user identified by an email
     *
     * @param email - said user's email
     * @throws ValidationException     - if said email is invalid
     * @throws AdministrationException - if a user with the specified email address doesn't exist
     *                                   or if it is already disabled
     */
    public void reactivateUserAccount(String email) throws ValidationException, com.map_toysocialnetworkgui.service.AdministrationException {
        changeUserAccountStatus(email,AccountStatus.ACTIVE);

    }

    /**
     * changes a user's account status (status must be valid)
     *
     * @param email  - said user's email
     * @param status - the new status
     * @throws ValidationException     - if said email is invalid
     * @throws AdministrationException - if a user with the specified email address doesn't exist
     */
    private void changeUserAccountStatus(String email, AccountStatus status)
            throws ValidationException, AdministrationException {

        userValidator.validateEmail(email);
        User user=usersRepo.get(email);
        if(user==null)
            throw new AdministrationException("Error: email not in use;\n");
        if(user.getAccountStatus().equals(status))
            throw new AdministrationException("Error: account already "+status.name()+";\n");
        user.setAccountStatus(status);
        usersRepo.update(user);
    }

    /**
     * returns a collection of all the users in repo
     *
     * @return said collection
     */
    public Iterable<User> getAllUsers() {
        return usersRepo.getAll();
    }

    /**
     * verifies if a list of emails contains only emails of existing users
     *
     * @param emailList - said email list
     * @throws ValidationException     - if any id is invalid
     * @throws AdministrationException - if any email doesn't belong to a user
     */
    public void verifyEmailCollection(Collection<String> emailList) throws ValidationException, AdministrationException {
        if(emailList==null)
            throw new ValidationException("Error: email list must be non null!;\n");
        List<String> errors=new ArrayList<>();
        emailList.forEach(email->{
            try{
                getUserInfo(email);
            }
            catch(AdministrationException|ValidationException e){
                errors.add("For email:"+email+"\n"+e.getMessage());
            }
        });
        StringBuilder error= new StringBuilder();
        for(String er:errors){
            error.append(er);
        }
        if(!error.toString().equals(""))
            throw new AdministrationException(error.toString());
    }

    /**
     * verifies if user email and password hash exists for logging in
     *
     * @param userEmail    - said user's email
     * @param userPassword - said user's password hash
     * @throws ValidationException     - if said user's email is invalid
     * @throws AdministrationException - if credentials are invalid
     * @return the user's info
     */
    public User login(String userEmail, int userPassword) throws ValidationException, AdministrationException {
        userValidator.validateEmail(userEmail);
        User found = usersRepo.get(userEmail);

        if (found == null || found.getPasswordHash() != userPassword)
            throw new AdministrationException("Invalid email or password!\n");
        return found;
    }
}
