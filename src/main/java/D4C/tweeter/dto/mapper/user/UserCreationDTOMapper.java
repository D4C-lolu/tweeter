package D4C.tweeter.dto.mapper.user;

import D4C.tweeter.dto.user.UserCreationDTO;
import D4C.tweeter.model.user.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserCreationDTOMapper {
    User DtoToUser(UserCreationDTO dto);
    UserCreationDTO userToDto(User user);
}
