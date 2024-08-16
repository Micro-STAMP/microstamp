package microstamp.authorization.service;

import microstamp.authorization.dto.UserReadDto;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    UserReadDto getMe(Jwt jwt);
}
