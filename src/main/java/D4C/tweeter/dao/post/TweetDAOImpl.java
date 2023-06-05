package D4C.tweeter.dao.post;


import D4C.tweeter.dto.mapper.post.TweetCreationDTOMapperImpl;
import D4C.tweeter.dto.mapper.post.TweetDTOMapperImpl;
import D4C.tweeter.dto.mapper.reaction.TweetReactionCreationDTOMapperImpl;
import D4C.tweeter.dto.mapper.reaction.TweetReactionDTOMapperImpl;
import D4C.tweeter.dto.mapper.user.UserDTOMapperImpl;
import D4C.tweeter.dto.post.TweetCreationDTO;
import D4C.tweeter.dto.post.TweetDTO;
import D4C.tweeter.dto.reaction.TweetReactionCreationDTO;
import D4C.tweeter.dto.reaction.TweetReactionDTO;
import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.post.QTweet;
import D4C.tweeter.model.post.Tweet;
import D4C.tweeter.model.reaction.QTweetReaction;
import D4C.tweeter.model.reaction.Reaction;
import D4C.tweeter.model.reaction.TweetReaction;
import D4C.tweeter.model.user.QUser;
import D4C.tweeter.model.user.User;
import D4C.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class TweetDAOImpl implements TweetDAO {

    private static final Logger logger = LogManager.getLogger(TweetDAOImpl.class);

    private static EntityManager entityManager = HibernateUtil.getSessionFactory().createEntityManager();

    private JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

    private TweetDTOMapperImpl tweeterDTOMapper = new TweetDTOMapperImpl();

    private TweetCreationDTOMapperImpl tweetCreationDTOMapper = new TweetCreationDTOMapperImpl();

    private TweetReactionDTOMapperImpl tweetReactionDTOMapper = new TweetReactionDTOMapperImpl();

    private TweetReactionCreationDTOMapperImpl tweetReactionCreationDTOMapper = new TweetReactionCreationDTOMapperImpl();

    private QTweet tweet = QTweet.tweet;

    private QUser user = QUser.user;

    private QTweetReaction tweetReaction = QTweetReaction.tweetReaction;

    @Override
    public TweetDTO getTweet(Long id) {
        Tweet t = queryFactory.selectFrom(tweet).where(tweet.id.eq(id)).fetchOne();
        TweetDTO tDTO = tweeterDTOMapper.tweetToDto(t);
        return tDTO;
    }

    @Override
    public List<TweetDTO> getAllTweets() {

        List<Tweet> tweets = queryFactory.selectFrom(tweet).orderBy(tweet.datePublished.desc()).fetch();
        List<TweetDTO> tweetDTOS = convertTweets(tweets);
        return tweetDTOS;
    }

    @Override
    public List<TweetDTO> getUserTweets(Long id) {

        List<Tweet> tweets = new ArrayList<>(queryFactory.select(user.tweets).from(user).where(user.id.eq(id)).orderBy(tweet.datePublished.desc()).fetchOne());
        List<TweetDTO> tweetDTOS = convertTweets(tweets);
        return tweetDTOS;
    }

    @Override
    public List<TweetReactionDTO> getTweetReactions(Long id) {
        List<TweetReaction> tweetReactions = new ArrayList<>(queryFactory.selectFrom(tweetReaction).where(tweetReaction.tweet.id.eq(id)).fetch());

        List<TweetReactionDTO> tweetReactionDTOS = convertTweetReactions(tweetReactions);
        return tweetReactionDTOS;

    }

    @Override
    public void addReaction(TweetReactionCreationDTO tweetReactionDTO) {

        Reaction reaction = tweetReactionDTO.getReaction();
        User u = queryFactory.selectFrom(user).where(user.id.eq(tweetReactionDTO.getUserId())).fetchFirst();
        Tweet t = queryFactory.selectFrom(tweet).where(tweet.id.eq(tweetReactionDTO.getTweetId())).fetchFirst();
        TweetReaction tr = new TweetReaction(u,t,reaction);
        Set<TweetReaction> tweetReactionSet = queryFactory.select(tweet.reactions)
                .from(tweet).where(tweet.id.eq(tr.getTweet().getId())).fetchFirst();
        if (tweetReactionSet.contains(tr)) {
            logger.info("Already reacted to this tweet");
            return;
        }
        executeInsideTransaction(queryFactory -> {
            queryFactory.insert(tweetReaction).set(tweetReaction.tweet, t)
                    .set(tweetReaction.user, u)
                    .set(tweetReaction.reaction, reaction).execute();
        });
    }

    @Override
    public void changeReaction(Long id, Reaction reaction) {
        executeInsideTransaction(queryFactory -> {
            queryFactory.update(tweetReaction).where(tweetReaction.id.eq(id)).set(tweetReaction.reaction, reaction).execute();
        });
    }

    @Override
    public void removeReaction(Long userId,Long tweetId) {
        executeInsideTransaction(queryFactory -> {
            queryFactory.delete(tweetReaction).where(tweetReaction.user.id.eq(userId))
                    .where(tweetReaction.tweet.id.eq(tweetId)).execute();
        });

    }

    @Override
    public void createTweet(String message, UserDTO user) {
        UserDTOMapperImpl userDTOMapper = new UserDTOMapperImpl();
        User u = userDTOMapper.DtoToUser(user);
        TweetCreationDTO tweetCreationDTO = new TweetCreationDTO(message,u);
        Tweet t = tweetCreationDTOMapper.DtoToTweet(tweetCreationDTO);
        executeInsideTransaction(queryFactory -> {
            queryFactory.insert(tweet).set(tweet.author, t.getAuthor()).set(tweet.body, t.getBody()).execute();
        });
    }

    @Override
    public List<TweetDTO> getTweetComments(Long id) {
        List<Tweet> tweets = new ArrayList<>(queryFactory.select(tweet.comments).from(tweet).where(tweet.id.eq(id))
                .orderBy(tweet.datePublished.asc()).fetchFirst());

        List<TweetDTO> tweetDTOS = convertTweets(tweets);
        return tweetDTOS;
    }

    @Override
    public void addComment(Long tweetId,String message, UserDTO user) {

        UserDTOMapperImpl userDTOMapper = new UserDTOMapperImpl();
        User u = userDTOMapper.DtoToUser(user);
        TweetCreationDTO tweetCreationDTO = new TweetCreationDTO(message,u);
        Tweet t = tweetCreationDTOMapper.DtoToTweet(tweetCreationDTO);
        executeInsideTransaction(queryFactory -> {
            Set<Tweet> tweetSet= queryFactory.select(tweet.comments)
                    .from(tweet).where(tweet.id.eq(tweetId)).fetchFirst();
            tweetSet.add(t);
            queryFactory.update(tweet).where(tweet.id.eq(tweetId)).set(tweet.comments, tweetSet);
        });
    }

    @Override
    public void deleteTweet(Long id) {
        executeInsideTransaction(queryFactory -> {
            queryFactory.delete(tweet).where(tweet.id.eq(id)).execute();
        });
    }

    private void executeInsideTransaction(Consumer<JPAQueryFactory> action) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.accept(queryFactory);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

    List<TweetDTO> convertTweets(List<Tweet> tweets) {
        if (tweets == null) {
            return null;
        }
        List<TweetDTO> tweetDTOs = new ArrayList<>();
        for (Tweet t : tweets) {
            TweetDTO tDTO = tweeterDTOMapper.tweetToDto(t);
            tweetDTOs.add(tDTO);
        }
        return tweetDTOs;
    }

    List<TweetReactionDTO> convertTweetReactions(List<TweetReaction> tweets) {
        if (tweets == null) {
            return null;
        }
        List<TweetReactionDTO> tweetDTOs = new ArrayList<>();
        for (TweetReaction t : tweets) {
            TweetReactionDTO tDTO = tweetReactionDTOMapper.tweetReactionToDto(t);
            tweetDTOs.add(tDTO);
        }
        return tweetDTOs;
    }
}
