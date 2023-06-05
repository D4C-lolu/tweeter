package D4C.application;

import D4C.tweeter.dto.post.TweetDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.service.post.TweetService;
import D4C.tweeter.service.user.UserService;
import D4C.util.FormatDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class Home {

    private static final Logger logger = LogManager.getLogger(Login.class);

    private static Scanner sc;

    public void start(UserDTO u){
        homeMessage(u);
        chooseOption(u);
    }

    private void homeMessage(UserDTO u) {
        logger.info("Welcome User {}", u.getUserName());
        logger.info("What would you like to do today?");
        logger.info("Enter 1 to see your feed");
        logger.info("Enter 2 to create a new tweet");
        logger.info("Enter 3 to see your own profile");
        logger.info("Enter 4 to explore other user profiles");
        logger.info("Or enter 0 to sign out");
        logger.info("What will it be? ");
    }

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
                    case 1:
                        openFeed(user);
                    case 2:
                        createTweet(user);
                    case 3:
                        viewProfile(user,user);
                    case 4:
                        viewGlobalProfiles(user);
                    default:
                        logger.warn("\nPlease enter a number between 1 and 4 ");
                        break;
                }
            } catch (final NumberFormatException e) {
                logger.warn("\nPlease enter a valid number: ");
            }
        }
        while (choice != 0);

    }

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
                viewTweet(user,tweetDTO);
            } catch (final NumberFormatException e) {
                logger.info("Invalid input");
                logger.info("Please enter a valid number");
            } catch (Exception e) {
                logger.info("Back to main menu ");
                chooseOption(user);
            }

        }
    }

    public void viewTweet(UserDTO user,TweetDTO tweetDTO) {
        if (tweetDTO == null) {
            logger.info("Selected tweet could not be found");
            return;
        }

        logger.info("Tweet by {} at {}", tweetDTO.getAuthor(), FormatDate.formatDate(tweetDTO.getDatePublished()));
        logger.info("{}", tweetDTO.getBody());
        logger.info("Likes: {}", TweetService.getInstance().getLikes(tweetDTO.getId()));
        logger.info("Dislikes: {}", TweetService.getInstance().getDisLikes(tweetDTO.getId()));
        logger.info("\n\n");
        List<TweetDTO> tweetDTOComments = TweetService.getInstance().getComments(tweetDTO.getId());
        if (tweetDTOComments == null || tweetDTOComments.size() == 0) {
            logger.info("Oops, no comments here");
            return;
        }
        int i = 1;
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
        logger.info("Enter A to go to the author's profile");
        logger.info("Enter L to like or D to dislike the tweet");
        logger.info("Enter the number next to a comment to go to its page");
        logger.info("or enter any key to go back");

        sc = new Scanner(System.in);
        String choice = "";

        choice = sc.nextLine();
        try {
            switch (choice) {
                case "a":
                case "A":
                    viewProfile(user, UserService.getInstance().getUser(tweetDTO.getAuthor().getId()));
                case "l":
                case "L":
                    TweetService.getInstance().like(user.getId(), tweetDTO.getId());
                case "d":
                case "D":
                    TweetService.getInstance().dislike(user.getId(), tweetDTO.getId());
                default:
                    int input = Integer.parseInt(choice);
                    TweetDTO t = tweetDTOComments.get(i - 1);
                    viewTweet(user, t);
            }
        } catch (final NumberFormatException e) {
            logger.info("Invalid input");
        } catch (Exception e) {
            logger.info("Back to main menu ");
        }
        chooseOption(user);
    }

    public void viewProfile(UserDTO user, UserDTO otherUser){
        logger.info("Viewing profile of user {}.",otherUser.getUserName());
        List<TweetDTO> tweetDTOList = TweetService.getInstance().getUserTweets(otherUser.getId());
        if (tweetDTOList==null || tweetDTOList.size()==0){
            logger.info("No tweets to be found here");
            return;
        }
        logger.info("User {} tweets: ",otherUser.getUserName());
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
            viewTweet(user,tweetDTO);
        } catch (final NumberFormatException e) {
            logger.info("Invalid input");
            logger.info("Please enter a valid number");
        } catch (Exception e) {
            logger.info("Back to main menu ");
            chooseOption(user);
        }
    }

    public void createTweet(UserDTO userDTO){
        sc = new Scanner(System.in);
        logger.info("Please Enter your post in less than 140 characters: ");
        String message = sc.nextLine();
        TweetService.getInstance().createTweet(message,userDTO.getId());
        logger.info("Your tweet has been published");

    }

    public void createComment(UserDTO userDTO, TweetDTO tweetDTO){
        sc = new Scanner(System.in);
        logger.info("Please Enter your post in less than 140 characters: ");
        String message = sc.nextLine();
        TweetService.getInstance().addComment(tweetDTO.getId(), message, userDTO.getId());
        logger.info("Your comment has been published");

    }
    public void viewGlobalProfiles(UserDTO user){
        logger.info("User Profiles: ");
        List<UserDTO> userDTOList = UserService.getInstance().getAllUsers();
        if(userDTOList==null || userDTOList.size()==0){
            logger.info("No active users");
            logger.info("Then who are you?");
            return;
        }
        logger.info("\n");
        int i = 1;
        for (UserDTO u : userDTOList) {
            int numofTweets = TweetService.getInstance().getUserTweets(u.getId()).size();
            logger.info("S/N: {}", i);
            logger.info("User {} with {} tweets",u.getUserName(),numofTweets);
            logger.info("\n");
            i++;
        }

        logger.info("Enter the number of the user to view their profile");
        logger.info("or enter any other key to go back");

        sc = new Scanner(System.in);
        String choice = "";

        choice = sc.nextLine();
        try {
            int input = Integer.parseInt(choice);
            UserDTO otherUser = userDTOList.get(i - 1);
            viewProfile(user,otherUser);
        } catch (final NumberFormatException e) {
            logger.info("Invalid input");
            logger.info("Please enter a valid number");
        } catch (Exception e) {
            logger.info("Back to main menu ");
            chooseOption(user);
        }



    }
}
