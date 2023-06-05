package D4C.tweeter.model.user;

/**
 * Enum representing the level of authorization a user has
 * USER - A regular user with the ability to create and delete their own posts,
 *          comment on posts and react tp posts/comments
 * MODERATOR - A user with administratuve privileges, able to carry out all user functions as well
 *             as delete other user's comments, posts or suspend them
 */
public enum Role {
    USER(0),
    MODERATOR(1);

    final int val;

    Role(int val){
        this.val = val;
    }
}
