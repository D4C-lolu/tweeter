package D4C.tweeter.dto.mapper.post;


import D4C.tweeter.dto.mapper.reaction.TweetReactionDTOMapper;
import D4C.tweeter.dto.post.TweetDTO;
import D4C.tweeter.dto.reaction.TweetReactionDTO;
import D4C.tweeter.model.post.Tweet;
import D4C.tweeter.model.reaction.TweetReaction;
import org.mapstruct.Mapper;

@Mapper(uses = {TweetReactionDTOMapper.class})
public interface TweetDTOMapper {
    Tweet DtoToTweet(TweetDTO tweetDTO);
    TweetDTO tweetToDto(Tweet tweet);

}
