package D4C.application;

import D4C.tweeter.dto.post.TweetDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.user.Role;
import D4C.tweeter.service.post.TweetService;
import D4C.tweeter.service.user.UserService;
import D4C.util.FormatDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

/**
 * Class representing the Application's Home page,
 * Contains all the functions that operate on the homepage
 */
public class Home {

    private static final Logger logger = LogManager.getLogger(Login.class);

    private static Scanner sc;

    public void start(UserDTO u) {
        homeMessage(u);
        chooseOption(u);
    }

    /**
     * Welcome message for Home screen
     * @param u-  object for currently logged in user
     */
    private void homeMessage(UserDTO u) {
        logger.info("Welcome User {}", u.getUserName());
        logger.info("What would you like to do today?");
        logger.info("Enter 1 to see your feed");
        logger.info("Enter 2 to create a new tweet");
        logger.info("Enter 3 to see your own profile");
        logger.info("Enter 4 to explore other user profiles");
        logger.info("Enter 5 to update your profile");
        logger.info("Or enter 0 to sign out");
        logger.info("What will it be? ");
    }

    /**
     * Main menu function
     * @param user - currently logged in user
     */
    private void chooseOption(UserDTO user) {
        sc = new Scanner(System.in);
        int choice = -1;
        do {
            try {
                String input = sc.nextLine();
                choice = Integer.parseInt(input);
                switch (choice) {
                    case 0:
                        UserService.getInstance().shutdown();
                        break;
                    case 1:
                        openFeed(user);
                        break;
                    case 2:
                        createTweet(user);
                        break;
                    case 3:
                        viewProfile(user, user);
                        break;
                    case 4:
                        viewGlobalProfiles(user);
                        break;
                    case 5:
                        updateUserDetails(user);
                    default:
                        logger.warn("\nPlease enter a number between 1 and 4 ");
                        break;
                }
            } catch (final NumberFormatException e) {
                logger.warn("\nPlease enter a valid number: ");
            }
        }
        while (choice != 0);
        UserService.getInstance().shutdown();
    }

    /**
     * Feed consisting of all tweets
     * @param user- currently logged in user
     */
    private void openFeed(UserDTO user) {
        List<TweetDTO> tweetDTOList = TweetService.getInstance().getFeed();
        if (tweetDTOList.size() == 0) {
            logger.info("No tweets available");
        } else {
            int i = 1;
            for (TweetDTO t : tweetDTOList) {
                logger.info("S/N: {}", i);
                logger.info("Tweet by {} at {}", t.getAuthor(), FormatDate.formatDate(t.getDatePublished()));
                logger.info("{}", t.getBody());
                logger.info("Likes: {}", TweetService.getInstance().getLikes(t.getId()));
                logger.info("Dislikes: {}", TweetService.getInstance().getDisLikes(t.getId()));
                logger.info("\n");
                i++;
            }

            logger.info("\n");
            logger.info("Enter the number of the tweet to view its comments");
            logger.info("or enter any other key to go back");

            sc = new Scanner(System.in);
            String choice = "";

            choice = sc.nextLine();
            try {
                int input = Integer.parseInt(choice);
                TweetDTO tweetDTO = tweetDTOList.get(i - 1);
                viewTweet(user, tweetDTO);
            } catch (final NumberFormatException e) {
                logger.info("Invalid input");
                logger.info("Please enter a valid number");
            } catch (Exception e) {
                logger.info("Back to main menu ");
                chooseOption(user);
            }

        }
    }

    /**
     * Method to view Tweet and Child Tweets
     * @param user - currently signed in user
     * @param tweetDTO - current Tweet
     */
    public void viewTweet(UserDTO user, TweetDTO tweetDTO) {
        if (tweetDTO == null) {
            logger.info("Selected tweet could not be found");
            return;
        }
        int i = 1;

        logger.info("Tweet by {} at {}", tweetDTO.getAuthor(), FormatDate.formatDate(tweetDTO.getDatePublished()));
        logger.info("{}", tweetDTO.getBody());
        logger.info("Likes: {}", TweetService.getInstance().getLikes(tweetDTO.getId()));
        logger.info("Dislikes: {}", TweetService.getInstance().getDisLikes(tweetDTO.getId()));
        logger.info("\n\n");
        List<TweetDTO> tweetDTOComments = TweetService.getInstance().getComments(tweetDTO.getId());
        if (tweetDTOComments == null || tweetDTOComments.size() == 0) {
            logger.info("Oops, no comments here");
        } else {

            for (TweetDTO t : tweetDTOComments) {
                logger.info("S/N: {}", i);
                logger.info("Tweet by {} at {}", t.getAuthor(), FormatDate.formatDate(t.getDatePublished()));
                logger.info("{}", t.getBody());
                logger.info("Likes: {}", TweetService.getInstance().getLikes(t.getId()));
                logger.info("Dislikes: {}", TweetService.getInstance().getDisLikes(t.getId()));
                logger.info("\n");
                i++;
            }
            logger.info("\n");
            logger.info("Enter the number next to a comment to go to its page");
        }

        logger.info("Enter A to go to the author's profile");
        logger.info("Enter L to like or D to dislike the tweet");
        logger.info("Enter C to comment on this tweet");

        //If User owns the tweet or is admin
        boolean owner = user.getRole().equals(Role.MODERATOR) || tweetDTO.getAuthor().getId().equals(user.getId());

        if (owner) {
            logger.info("Enter X to delete this tweet");
        }
        logger.info("or enter any key to go back");

        sc = new Scanner(System.in);
        String choice = "";

        choice = sc.nextLine();
        try {
            switch (choice) {
                case "a":
                case "A":
                    viewProfile(user, UserService.getInstance().getUser(tweetDTO.getAuthor().getId()));
                    break;
                case "l":
                case "L":
                    TweetService.getInstance().like(user.getId(), tweetDTO.getId());
                    break;
                case "d":
                case "D":
                    TweetService.getInstance().dislike(user.getId(), tweetDTO.getId());
                    break;
                case "c":
                case "C":
                    createComment(user, tweetDTO);
                    break;
                case "x":
                case "X":
                    if (owner) {
                        TweetService.getInstance().deleteTweet(tweetDTO.getId());
                    }
                    //Go back to Menu
                    chooseOption(user);
                    break;
                default:
                    int input = Integer.parseInt(choice);
                    TweetDTO t = tweetDTOComments.get(i - 1);
                    viewTweet(user, t);
                    break;
            }
        } catch (final NumberFormatException e) {
            logger.info("Invalid input");
        } catch (Exception e) {
            logger.info("Back to main menu ");
        }
        chooseOption(user);
    }

    /**
     * Method to view a user's profile
     * @param user - currently signed in user
     * @param otherUser - other user object
     */
    public void viewProfile(UserDTO user, UserDTO otherUser) {
        if (user.equals(otherUser)) {
            logger.info("Viewing profile of user {}.", otherUser.getUserName());
        }
        if (user.getRole().equals(Role.MODERATOR)) {
            logger.info("*_MOD_*");
        }

        int i = 1;
        List<TweetDTO> tweetDTOList = TweetService.getInstance().getUserTweets(otherUser.getId());
        if (tweetDTOList == null || tweetDTOList.size() == 0) {
            logger.info("No tweets to be found here");

        } else {
            logger.info("User {} tweets: ", otherUser.getUserName());

            for (TweetDTO t : tweetDTOList) {
                logger.info("S/N: {}", i);
                logger.info("Tweet by {} at {}", t.getAuthor(), FormatDate.formatDate(t.getDatePublished()));
                logger.info("{}", t.getBody());
                logger.info("Likes: {}", TweetService.getInstance().getLikes(t.getId()));
                logger.info("Dislikes: {}", TweetService.getInstance().getDisLikes(t.getId()));
                logger.info("\n");
                i++;
            }

            logger.info("\n");
            logger.info("Enter the number of the tweet to view its comments");
        }
        if (user.equals(otherUser)) {
            logger.info("Enter F to see your followers");
            logger.info("Enter N to see accounts you follow");
        } else {
            logger.info("Enter F to see the user's followers");
            logger.info("Enter N to see accounts this user follows");
            logger.info("Enter V to follow this user");
            logger.info("Enter X to unfollow this user");
        }
        if (user.getRole().equals(Role.MODERATOR)) {
            logger.info("Enter B to ban this user");
            logger.info("Enter U to elevate this user's privileges");
            logger.info("Enter D to downgrade this user's privileges");
        }

        logger.info("or enter any other key to go back");

        sc = new Scanner(System.in);
        String choice = "";

        choice = sc.nextLine();

        try {
            switch (choice) {
                case "v":
                case "V":
                    if (!user.equals(otherUser)) {
                        UserService.getInstance().followUser(user.getId(), otherUser.getId());
                    }
                    break;
                case "x":
                case "X":
                    if (!user.equals(otherUser)) {
                        UserService.getInstance().unfollowUser(user.getId(), otherUser.getId());
                    }
                    break;
                case "f":
                case "F":
                    logger.info("{}'s followers", otherUser.getUserName());
                    List<UserDTO> followersList = UserService.getInstance().getFollowers(otherUser.getId());
                    viewUserProfiles(user, followersList);
                    break;
                case "n":
                case "N":
                    logger.info("Account {} follows", otherUser.getUserName());
                    List<UserDTO> followingList = UserService.getInstance().getFollowing(otherUser.getId());
                    viewUserProfiles(user, followingList);
                    break;
                case "B":
                case "b":
                    if (user.getRole().equals(Role.MODERATOR)) {
                        UserService.getInstance().updateActiveStatus(otherUser.getId(), false);
                    }
                    //Go back to Menu
                    chooseOption(user);
                    break;
                case "U":
                case "u":
                    if (user.getRole().equals(Role.MODERATOR)) {
                        UserService.getInstance().updateRole(otherUser.getId(), Role.MODERATOR);
                    }
                    //Go back to Menu
                    chooseOption(user);
                    break;
                case "D":
                case "d":
                    if (user.getRole().equals(Role.MODERATOR)) {
                        UserService.getInstance().updateRole(otherUser.getId(), Role.USER);
                    }
                    //Go back to Menu
                    chooseOption(user);
                    break;
                default:
                    int input = Integer.parseInt(choice);
                    TweetDTO tweetDTO = tweetDTOList.get(i - 1);
                    viewTweet(user, tweetDTO);
                    break;
            }
        } catch (final NumberFormatException e) {
            logger.info("Invalid input");
            logger.info("Please enter a valid number");
        } catch (Exception e) {
            logger.info("Back to main menu ");
        }
        chooseOption(user);
    }

    public void createTweet(UserDTO userDTO) {
        sc = new Scanner(System.in);
        logger.info("Please Enter your post in less than 140 characters: ");
        String message = sc.nextLine();
        TweetService.getInstance().createTweet(message, userDTO.getId());
        logger.info("Your tweet has been published");

    }

    public void createComment(UserDTO userDTO, TweetDTO tweetDTO) {
        sc = new Scanner(System.in);
        logger.info("Please Enter your post in less than 140 characters: ");
        String message = sc.nextLine();
        TweetService.getInstance().addComment(tweetDTO.getId(), message, userDTO.getId());
        logger.info("Your comment has been published");

    }

    public void viewGlobalProfiles(UserDTO user) {
        logger.info("User Profiles: ");
        List<UserDTO> userDTOList = UserService.getInstance().getAllUsers();

        if (userDTOList == null || userDTOList.size() == 0) {
            logger.info("No active users");
            logger.info("Then who are you?");
            return;
        }
        viewUserProfiles(user, userDTOList);
    }

    public void viewUserProfiles(UserDTO user, List<UserDTO> userDTOList) {
        int i = 1;
        if (userDTOList == null || userDTOList.size() == 0) {
            logger.info("Oops, nothing to see here");
        } else {
            for (UserDTO u : userDTOList) {
                int numofTweets = TweetService.getInstance().getUserTweets(u.getId()).size();
                logger.info("S/N: {}", i);
                logger.info("User {} with {} tweets", u.getUserName(), numofTweets);
                if (user.getRole().equals(Role.MODERATOR)) {
                    logger.info("*_MOD_*");
                }
                logger.info("\n");
                i++;
            }
            logger.info("\n");
        }

        logger.info("Enter the number of the user to view their profile");
        logger.info("or enter any other key to go back");

        sc = new Scanner(System.in);
        String choice = "";

        choice = sc.nextLine();
        try {
            int input = Integer.parseInt(choice);
            UserDTO otherUser = userDTOList.get(i - 1);
            viewProfile(user, otherUser);
        } catch (final NumberFormatException e) {
            logger.info("Invalid input");
            logger.info("Please enter a valid number");
        } catch (Exception e) {
            logger.info("Back to main menu ");
            chooseOption(user);
        }
    }

    public void updateUserDetails(UserDTO user) {
        logger.info("User update page: ");
        logger.info("Enter U to update your username");
        logger.info("Enter P to update your password");
        logger.info("Enter D to delete your account");
        logger.info("Or any other key to go back");

        sc = new Scanner(System.in);
        String choice = "";

        choice = sc.nextLine();
        try {
            switch (choice) {
                case "u":
                case "U":
                    logger.info("Enter your new username here:");
                    String name = sc.nextLine();
                    UserService.getInstance().updateUsername(user.getId(),name);
                    logger.info("Username has been updated");
                    break;
                case "p":
                case "P":
                    logger.info("Enter your new password here:");
                    String password = sc.nextLine();
                    UserService.getInstance().updateUsername(user.getId(),password);
                    logger.info("Your password has been updated");
                    break;
                case "D":
                case "d":
                    logger.info("Are you sure you wish to delete your account");
                    logger.info("Enter Y to continue  and any other key to abort");
                    String ans = sc.nextLine();
                    if (ans.equals("y")){
                        UserService.getInstance().deleteAccount(user.getId());
                        logger.info("Goodbye!");
                        UserService.getInstance().shutdown();
                    }
                    break;
            }
        }catch(Exception e){
            logger.info("Invalid input");
            logger.info("Back to main menu ");
        }

         chooseOption(user);
    }

}
