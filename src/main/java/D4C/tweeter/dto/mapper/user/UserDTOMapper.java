package D4C.tweeter.dto.mapper.user;

import D4C.tweeter.dto.user.UserDTO;
import D4C.tweeter.model.user.User;
import org.mapstruct.Mapper;


@Mapper
public interface UserDTOMapper {
    User DtoToUser(UserDTO dto);
    UserDTO userToDto(User user);
}
