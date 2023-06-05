package D4C.tweeter.service.post;


import D4C.tweeter.dao.post.TweetDAO;
import D4C.tweeter.dao.post.TweetDAOImpl;
import D4C.tweeter.dto.post.TweetCreationDTO;
import D4C.tweeter.dto.post.TweetDTO;
import D4C.tweeter.dto.reaction.TweetReactionCreationDTO;
import D4C.tweeter.dto.reaction.TweetReactionDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.reaction.Reaction;
import D4C.tweeter.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TweetService {
    private static final Logger logger = LogManager.getLogger(TweetService.class);
    private static final TweetService _instance;

    private static TweetDAOImpl tweetDAO;

    private TweetService(){
        this.tweetDAO = new TweetDAOImpl();
    }

    static {
        try {
            _instance = new TweetService();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong creating the Tweetservice");
        }
    }

    public static TweetService getInstance() {
        return _instance;
    }
    public List<TweetDTO> getFeed(){
        List<TweetDTO> tweetDTOS = tweetDAO.getAllTweets();
        tweetDTOS.sort(Comparator.comparing(t -> t.getDatePublished()));
        return tweetDTOS;
    }

    public TweetDTO getTweet(Long id){
        return tweetDAO.getTweet(id);
    }

    public List<TweetDTO> getComments(Long id){
        return tweetDAO.getTweetComments(id);
    }

    public List<TweetReactionDTO> getReactions(Long id){
        return tweetDAO.getTweetReactions(id);
    }

    public List<TweetDTO> getUserTweets(Long id){
        return tweetDAO.getUserTweets(id);
    }

    public void like(Long userId, Long tweetId){
        TweetReactionCreationDTO trDTO = new TweetReactionCreationDTO(userId,tweetId,Reaction.LIKE);
        tweetDAO.removeReaction(userId,tweetId);
        tweetDAO.addReaction(trDTO);
        logger.info("Tweet added to likes");
    }

    public void dislike(Long userId, Long tweetId){
        TweetReactionCreationDTO trDTO = new TweetReactionCreationDTO(userId,tweetId,Reaction.DISLIKE);
        tweetDAO.addReaction(trDTO);
        tweetDAO.removeReaction(userId,tweetId);
        logger.info("Tweet disliked");
    }

    public void unlike(Long userId,long tweetId){
        tweetDAO.removeReaction(userId,tweetId);
    }

    public void createTweet(String body, Long userId){

        UserDTO user = UserService.getInstance().getUser(userId);
        tweetDAO.createTweet(body,user);
        logger.info("Tweet posted successfully");
    }

    public void deleteTweet(Long id){
        tweetDAO.deleteTweet(id);
        logger.info("Tweet deleted successfully");
    }

    public void addComment(Long tweetId,String message,Long userId){
        UserDTO user = UserService.getInstance().getUser(userId);
        tweetDAO.addComment(tweetId,message,user);
        logger.info("Comment posted successfully");
    }

    public int getLikes(Long id){
        int likes = tweetDAO.getTweetReactions(id).stream().
                filter(t->t.getReaction().equals(Reaction.LIKE)).collect(Collectors.toSet()).size();
        return likes;
    }

    public int getDisLikes(Long id){
        int dislikes = tweetDAO.getTweetReactions(id).stream().
                filter(t->t.getReaction().equals(Reaction.DISLIKE)).collect(Collectors.toSet()).size();
        return dislikes;
    }
}
