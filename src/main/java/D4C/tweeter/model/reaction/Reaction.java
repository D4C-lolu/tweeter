package D4C.tweeter.model.reaction;

/**
 * Enum representing a User's reaction to a Post or Comment
 */
public enum Reaction {
    LIKE(0),
    DISLIKE(1);

    final int val;

    Reaction(int val){
        this.val = val;
    }
}
