package D4C.application;

import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.user.Role;
import D4C.tweeter.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import  java.util.regex.Pattern;
import java.util.Scanner;

/**
 * Login page
 * Contains methods to log in
 */
public class Login {
    private static final Logger logger = LogManager.getLogger(Login.class);

    private static Scanner sc;

    private int welcome() {
        logger.info("Enter 1 to sign in");
        logger.info("Enter 2 to create a new account");
        logger.info("or Enter 0 to terminate the program");
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
                        return 1;
                    case 2:
                        return 2;
                    default:
                        logger.warn("\nPlease enter either 1 or 2 ");
                        break;
                }

            } catch (final NumberFormatException e) {
                logger.warn("\nPlease enter a valid number: ");
            }
        }
        while (choice != 0);

        return -1;
    }

    private UserDTO signIn() {
        sc = new Scanner(System.in);
        logger.info("Please enter your phonenumber and password to sign in");
        String[] userDetails = new String[2];
        UserDTO user = null;

        do {
            try {
                logger.info("Phone number: ");
                userDetails[0] = sc.nextLine();
                logger.info("Password: ");
                userDetails[1] = sc.nextLine();

                user = UserService.getInstance().signIn(userDetails[0], userDetails[1]);

                if (user != null) {
                    break;
                }
                logger.info("Invalid password or user");
                logger.info("Press any key to end the program or '/' to try again");
                String next = sc.nextLine();
                if (next.equals("/")){
                    continue;
                }
                else{
                    UserService.getInstance().shutdown();
                }

            } catch (final Exception e) {
                logger.error("The details above do not match any user");
                logger.info("Please enter valid user details: ");
            }
        }
        while (true);
        return user;
    }


    private UserDTO signup(){
        sc = new Scanner(System.in);

       Pattern pattern =Pattern.compile("(\\+234|0)[0-9]{10}");
        logger.info("Welcome new user.");
        logger.info("Please enter your details when prompted to create a new account");
        String[] userDetails = new String[3];
        UserDTO user = null;

        do {
            try {
                logger.info("Phone number: ");
                userDetails[0] = sc.nextLine();
                if (!userDetails[0].matches("(\\+234|0)[0-9]{10}")){
                    logger.info("Invalid phone number");
                    logger.info("Please try again");
                }
                logger.info("User name: ");
                userDetails[1] = sc.nextLine();
                logger.info("Password: ");
                userDetails[2] = sc.nextLine();

                user = UserService.getInstance().signup(userDetails[1], userDetails[2],userDetails[0], Role.USER);

                if (user != null) {
                    break;
                }
                logger.info("Error in sign up process");
                logger.info("Press any key to end the program or '/' to try again");
                String next = sc.nextLine();
                if (next.equals("/")){
                    continue;
                }
                else{
                    UserService.getInstance().shutdown();
                }

            } catch (final Exception e ) {
                logger.error("Invalid input");
                logger.info("Please enter valid user details: ");
            }
        }
        while (true);
        return user;
    }

    public  UserDTO run(){
        int choice = welcome();
        switch(choice){
            case 1:
                return signIn();
            case 2:
                return signup();
            default:
                UserService.getInstance().shutdown();
        }
        return null;

    }

}
