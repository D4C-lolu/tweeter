package D4C.tweeter.service.user;


import D4C.tweeter.dao.user.UserDAOImpl;
import D4C.tweeter.dto.user.UserCreationDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.user.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Class containing methods that operate on the User Class
 */
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private static final UserService _instance;

    private static UserDAOImpl userDAO;

    private UserService(){
        this.userDAO = new UserDAOImpl();
    }

    static {
        try {
            _instance = new UserService();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong creating the UserService");
        }
    }

    public static UserService getInstance() {
        return _instance;
    }

    public UserDTO signup(String userName, String password, String phoneNumber, Role role ){
        UserCreationDTO userCreationDTO = new UserCreationDTO(userName,password,phoneNumber,role);

        userDAO.saveUser(userCreationDTO);
        logger.info("User account created.");

        return signIn(userName,password);
    }

    public UserDTO signIn(String userName, String password){
        UserDTO userDTO = userDAO.signIn(userName,password);
        if (userDTO!=null){
            userDAO.updateLastseen(userDTO.getId());
        }
        return userDTO;
    }

    public void signout(Long id){
        userDAO.updateLastseen(id);
        shutdown();
    }

    public void shutdown(){
        logger.info("Exiting the application");
        System.exit(0);
    }

    public void deleteAccount(Long id){
        userDAO.deleteUser(id);
        logger.info("Account deleted!");
    }

    public List<UserDTO> getAllUsers(){
        return userDAO.getAll();
    }

    public List<UserDTO> getFollowers(Long id){
        return userDAO.getUserFollowers(id);
    }

    public List<UserDTO> getFollowing(Long id){
        return userDAO.getUserFollowing(id);
    }

    public UserDTO getUser(Long id){
        return userDAO.getUser(id);
    }

    public void updatePassword(Long id, String newPass){
         userDAO.updatePassword(id,newPass);
    }

    public void updateUsername(Long id,String name){
        userDAO.updateUsername(id,name);
        logger.info("Name change successful");
    }

    public void updateRole(Long id, Role role){
        userDAO.updateRole(id, role);
        logger.info("Role change successful");
    }

    public void updateActiveStatus(Long id, boolean status){
        userDAO.updateActiveStatus(id,status);
        logger.info("Status changed!");
    }

    public void followUser(Long userId,Long followeeId){
        userDAO.followUser(userId,followeeId);
    }

    public void unfollowUser(Long userId,Long followeeId){
        userDAO.unfollowUser(userId,followeeId);
    }


}

