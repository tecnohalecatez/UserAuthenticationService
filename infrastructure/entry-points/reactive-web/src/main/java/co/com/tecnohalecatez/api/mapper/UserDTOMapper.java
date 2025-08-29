package co.com.tecnohalecatez.api.mapper;

import co.com.tecnohalecatez.api.dto.UserDTO;
import co.com.tecnohalecatez.api.dto.UserDataDTO;
import co.com.tecnohalecatez.model.user.User;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    UserDTO toResponse(User user);

    User toModel(UserDataDTO userDataDTO);

    default Flux<UserDTO> toResponseFlux(Flux<User> users) {
        return users.map(this::toResponse);
    }
}
