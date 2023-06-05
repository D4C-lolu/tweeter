package D4C.tweeter.dto.reaction;


import D4C.tweeter.dto.post.TweetDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.reaction.Reaction;
import org.mapstruct.Mapper;

import java.util.Objects;

@Mapper
public class TweetReactionCreationDTO {
    private Long userId;
    private Long tweetId;
    private Reaction reaction;

    public TweetReactionCreationDTO(){

    }

    public TweetReactionCreationDTO(Long userId, Long tweetId, Reaction reaction) {
        this.userId = userId;
        this.tweetId = tweetId;
        this.reaction = reaction;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
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
        if (!(o instanceof TweetReactionCreationDTO)) return false;
        TweetReactionCreationDTO that = (TweetReactionCreationDTO) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getTweetId(), that.getTweetId()) && getReaction() == that.getReaction();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getTweetId(), getReaction());
    }

    @Override
    public String toString() {
        return "TweetReactionCreationDTO{" +
                "userId=" + userId +
                ", tweetId=" + tweetId +
                ", reaction=" + reaction +
                '}';
    }
}
