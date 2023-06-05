package D4C.tweeter.dao.user;

import D4C.application.Application;
import D4C.tweeter.dto.mapper.user.UserCreationDTOMapperImpl;
import D4C.tweeter.dto.mapper.user.UserDTOMapperImpl;
import D4C.tweeter.dto.user.UserCreationDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.user.QUser;
import D4C.tweeter.model.user.Role;
import D4C.tweeter.model.user.User;
import D4C.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Implementation class for User Data Access Object
 */
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

    private static EntityManager entityManager = HibernateUtil.getSessionFactory().createEntityManager();

    private JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

    private UserDTOMapperImpl userDTOMapper = new UserDTOMapperImpl();

    private UserCreationDTOMapperImpl userCreationDTOMapper = new UserCreationDTOMapperImpl();

    private QUser user = QUser.user;


    @Override
    public UserDTO getUser(Long id) {

        User u = queryFactory.selectFrom(user)
                .where(user.id.eq(id))
                .fetchOne();
        UserDTO uDTO = userDTOMapper.userToDto(u);

        return uDTO;
    }

    @Override
    public List<UserDTO> getAll() {

        List<User> users = queryFactory.selectFrom(user)
                .orderBy(user.lastSeen.asc())
                .fetch();

        List<UserDTO> userDTOS = convertUsers(users);
        return userDTOS;
    }

    @Override
    public UserDTO signIn(String phonenumber, String password){
        User u = queryFactory.selectFrom(user)
                .where(user.phoneNumber.eq(phonenumber))
                .fetchOne();
        if (u.getPassword().equals(password)){
            UserDTO uDTO = userDTOMapper.userToDto(u);
            return uDTO;
        }
        return null;
    }

    @Override
    public void saveUser(UserCreationDTO uDTO) {
        User u = userCreationDTOMapper.DtoToUser(uDTO);
        executeInsideTransaction(queryFactory -> {
            queryFactory.insert(user).set(user.userName, u.getUserName())
                    .set(user.phoneNumber, u.getPhoneNumber())
                    .set(user.password, u.getPassword())
                    .set(user.role, u.getRole()).execute();
        });
    }


    @Override
    public void updatePassword(Long id, String newPassword) {
        executeInsideTransaction(queryFactory -> {
            queryFactory.update(user)
                    .where(user.id.eq(id))
                    .set(user.password, newPassword).execute();
        });
    }


    @Override
    public void updateRole(Long id, Role role) {
        executeInsideTransaction(queryFactory -> {
            queryFactory.update(user)
                    .where(user.id.eq(id))
                    .set(user.role, role).execute();
        });
    }

    @Override
    public void updateUsername(Long id, String newName) {
        executeInsideTransaction(queryFactory -> {
            queryFactory.update(user)
                    .where(user.id.eq(id))
                    .set(user.userName, newName).execute();
        });
    }

    @Override
    public void updateLastseen(Long id) {
        executeInsideTransaction(queryFactory -> {
            queryFactory.update(user)
                    .where(user.id.eq(id))
                    .set(user.lastSeen, LocalDateTime.now()).execute();
        });
    }

    @Override
    public void updateActiveStatus(Long id, boolean status) {
        executeInsideTransaction(queryFactory -> {
            queryFactory.update(user)
                    .where(user.id.eq(id))
                    .set(user.active, status).execute();
        });
    }

    @Override
    public void deleteUser(Long id) {
        executeInsideTransaction(queryFactory -> {
            queryFactory.delete(user)
                    .where(user.id.eq(id))
                    .execute();
        });
    }

    @Override
    public List<UserDTO> getUserFollowers(Long id) {
        List<User> users = new ArrayList<>(queryFactory.select(user.followers)
                .from(user).where(user.id.eq(id))
                .fetchFirst());

        List<UserDTO> userDTOS = convertUsers(users);
        return userDTOS;
    }

    @Override
    public List<UserDTO> getUserFollowing(Long id) {
        List<User> users = new ArrayList<>(queryFactory.select(user.following)
                .from(user).where(user.id.eq(id))
                .fetchFirst());

        List<UserDTO> userDTOS = convertUsers(users);
        return userDTOS;
    }

    @Override
    public void followUser(Long userId,Long followeeId){

        Set<User> userSet = queryFactory.select(user.following)
                .from(user).where(user.id.eq(followeeId)).fetchFirst();
        Set<Long> ids = userSet.stream().map(i->i.getId()).collect(Collectors.toSet());
        if(ids.contains(userId)){
            logger.info("Already following!");
            return;
        }
        User u = queryFactory.selectFrom(user).where(user.id.eq(userId)).fetchFirst();
        userSet.add(u);
        executeInsideTransaction(queryFactory->{
            queryFactory.update(user).where(user.id.eq(userId))
                    .set(user.following,userSet);
        });
    }

    @Override
    public void unfollowUser(Long userId , Long followeeId){
        Set<User> userSet = queryFactory.select(user.following)
                .from(user).where(user.id.eq(followeeId)).fetchFirst();
        Set<Long> ids = userSet.stream().map(i->i.getId()).collect(Collectors.toSet());
        if(!ids.contains(userId)){
            logger.info("Not following!");
            return;
        }
        User u = queryFactory.selectFrom(user).where(user.id.eq(userId)).fetchFirst();
        userSet.remove(u);
        executeInsideTransaction(queryFactory->{
            queryFactory.update(user).where(user.id.eq(userId))
                    .set(user.following,userSet);
        });
    }

    /**
     * Method wraps update and delete methods to allow transactional operations
     *
     * @param action
     */
    private void executeInsideTransaction(Consumer<JPAQueryFactory> action) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.accept(queryFactory);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

    List<UserDTO> convertUsers(List<User> users) {
        if(users == null) {
            return null;
        }
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User u : users) {
            UserDTO uDTO = userDTOMapper.userToDto(u);
            userDTOs.add(uDTO);
        }
        return userDTOs;
    }

}
