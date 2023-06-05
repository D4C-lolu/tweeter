package D4C.tweeter.dao.post;

import D4C.tweeter.dto.post.TweetCreationDTO;
import D4C.tweeter.dto.post.TweetDTO;
import D4C.tweeter.dto.reaction.TweetReactionCreationDTO;
import D4C.tweeter.dto.reaction.TweetReactionDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.reaction.Reaction;

import java.util.List;

/**
 * Interface for Tweet Data Access Object
 */
public interface TweetDAO {
    TweetDTO getTweet(Long id);

    List<TweetDTO> getAllTweets();

    List<TweetDTO> getUserTweets(Long id);

    List<TweetReactionDTO> getTweetReactions(Long id);

    void addReaction(TweetReactionCreationDTO tweetReactionDTO);

    void changeReaction(Long id, Reaction reaction);

    void removeReaction(Long userId,Long tweetId);

    void createTweet(String message, UserDTO user);

    List<TweetDTO> getTweetComments(Long id);

    void addComment(Long tweetId, String message, UserDTO user);

    void deleteTweet(Long id);

}
