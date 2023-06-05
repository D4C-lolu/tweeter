package D4C.tweeter.dao.user;

import D4C.tweeter.dto.user.UserCreationDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.user.Role;

import java.util.Collection;

/**
 * Interface for User Data Access Object
 */
public interface UserDAO {

    /**
     * Method returns a User DTO containing the details of the User
     * whose id was passed in
     * @param id - User id
     * @return - DTO containg user details
     */
    UserDTO getUser(Long id);

    /**
     * Method returns a collection of DTOs of all Users in the DB
     * @return List containing user DTOs
     */
    Collection<UserDTO> getAll();

    /**
     * Method saves a user object using a DTO
     * @param uDTO - DTO containing User details
     */
    void saveUser(UserCreationDTO uDTO);

    /**
     * Method validates a user by comparing input password with user password
     * @param phonenumber - User phone number
     * @param password - user password
     * @return - DTO containg user details
     */
    UserDTO signIn(String phonenumber,String password);

    /**
     * Method updates a user object's password
     * @param id - User id
     * @param newPassword - new password string
     */
    void updatePassword(Long id, String newPassword);

    /**
     * Method updates a user object's role
     * @param id - User id
     * @param role - new Role
     */
    void updateRole(Long id, Role role);

    /**
     * Method updates a user object's username
     * @param id - User id
     * @param newName - new Username
     */
    void updateUsername(Long id, String newName);

    /**
     * Method updates a user object's last login date
     * @param id - User id
     */
    void updateLastseen(Long id);

    /**
     * Method updates a user object's active status
     * @param id - User id
     * @param status - new activity status
     */
    void updateActiveStatus(Long id, boolean status);

    /**
     * Method deletes the user object
     * @param id - user id
     */
    void deleteUser(Long id);

    /**
     * Method gets all the user objects following the user whose id is passed in
     * @param id - User id
     * @return - collection of user objects following user
     */
    Collection<UserDTO> getUserFollowers(Long id);

    /**
     * Method gets all the user objects whom the user whose id is passed in follows
     * @param id - User id
     * @return - collection of user objects the user follows
     */
    Collection<UserDTO> getUserFollowing(Long id);

    /**
     * Method adds a user to the list of followed user objects belonging to the user
     * @param userId - user id
     * @param followeeId -id of account to be followed
     */
    void followUser(Long userId,Long followeeId);

    /**
     * Method removes a user from the list of followed user objects belonging to the user
     * @param userId - user id
     * @param followeeId -id of account to be unfollowed
     */
    void unfollowUser(Long userId,Long followeeId);
}
