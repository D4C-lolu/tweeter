package D4C.tweeter.dto.mapper.reaction;


import D4C.tweeter.dto.mapper.post.TweetDTOMapper;
import D4C.tweeter.dto.mapper.user.UserDTOMapper;
import D4C.tweeter.dto.reaction.TweetReactionDTO;
import D4C.tweeter.model.reaction.TweetReaction;
import org.mapstruct.Mapper;

@Mapper(uses = {UserDTOMapper.class, TweetDTOMapper.class})
public interface TweetReactionDTOMapper {
    TweetReaction DtoToTweetReaction(TweetReactionDTO dto);

    TweetReactionDTO tweetReactionToDto(TweetReaction tr);

}
