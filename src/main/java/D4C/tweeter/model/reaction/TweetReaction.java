package D4C.tweeter.model.reaction;

import D4C.tweeter.model.user.User;
import D4C.tweeter.model.post.Tweet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * A class representing a user's reaction to a tweet. A user may like/dislike a tweet
 */
@Entity
@Table(name="like")
public class TweetReaction {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tweet_id", referencedColumnName = "id")
    private Tweet tweet;

    @NotNull
    @Convert(converter = ReactionAttributeConverter.class)
    @Column(name = "reaction")
    private Reaction reaction;

    public TweetReaction(){

    }

    /**
     *
     * @param id - Tweet id
     * @param user- User reaction belongs to
     * @param tweet - Tweet reaction belongs to
     * @param reaction - Enum representing the user's reaction
     */
    public TweetReaction(Long id, User user, Tweet tweet, Reaction reaction) {
        this.id = id;
        this.user = user;
        this.tweet = tweet;
        this.reaction = reaction;
    }

    /**
     * @param user- User reaction belongs to
     * @param tweet - Tweet reaction belongs to
     * @param reaction - Enum representing the user's reaction
     */
    public TweetReaction(User user, Tweet tweet, Reaction reaction) {
        this.id = id;
        this.user = user;
        this.tweet = tweet;
        this.reaction = reaction;
    }

    public Long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }


    public Tweet getTweet() {
        return tweet;
    }


    public Reaction getReaction() {
        return reaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TweetReaction)) return false;
        TweetReaction that = (TweetReaction) o;
        return   Objects.equals(getUser(), that.getUser()) && Objects.equals(getTweet(), that.getTweet()) && getReaction() == that.getReaction();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getTweet(), getReaction());
    }

    @Override
    public String toString() {
        return "TweetReaction{" +
                "id=" + id +
                ", user=" + user +
                ", tweet=" + tweet +
                ", reaction=" + reaction +
                '}';
    }
}
