package D4C.application;

import D4C.tweeter.dto.user.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void start() {
        Login login = new Login();
        Home home = new Home();

        logger.info("Welcome to Tweeter");
        UserDTO user = login.run();
        home.start(user);

    }
}
