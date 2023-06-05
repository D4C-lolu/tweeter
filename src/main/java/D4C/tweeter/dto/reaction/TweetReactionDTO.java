package D4C.tweeter.dto.reaction;

import D4C.tweeter.dto.post.TweetDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.post.Tweet;
import D4C.tweeter.model.reaction.Reaction;
import D4C.tweeter.model.user.User;

import java.util.Objects;

public class TweetReactionDTO {
    private Long id;
    private UserDTO user;
    private TweetDTO tweet;
    private Reaction reaction;

    public TweetReactionDTO(){

    }

    public TweetReactionDTO(UserDTO user, TweetDTO tweet, Reaction reaction) {
        this.user = user;
        this.tweet = tweet;
        this.reaction = reaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public TweetDTO getTweet() {
        return tweet;
    }

    public void setTweet(TweetDTO tweet) {
        this.tweet = tweet;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TweetReactionDTO)) return false;
        TweetReactionDTO that = (TweetReactionDTO) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getTweet(), that.getTweet()) && getReaction() == that.getReaction();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getTweet(), getReaction());
    }

    @Override
    public String toString() {
        return "TweetReactionDTO{" +
                "id=" + id +
                ", user=" + user +
                ", tweet=" + tweet +
                ", reaction=" + reaction +
                '}';
    }
}
