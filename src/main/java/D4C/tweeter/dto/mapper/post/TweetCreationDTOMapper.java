package D4C.tweeter.dto.mapper.post;

import D4C.tweeter.dto.post.TweetCreationDTO;
import D4C.tweeter.model.post.Tweet;
import org.mapstruct.Mapper;

@Mapper
public interface TweetCreationDTOMapper {
    Tweet DtoToTweet(TweetCreationDTO tweetDTO);
    TweetCreationDTO tweetReactionToDto(Tweet tweet);
}
