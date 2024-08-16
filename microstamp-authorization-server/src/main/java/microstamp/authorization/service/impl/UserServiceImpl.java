package microstamp.authorization.service.impl;

import microstamp.authorization.dto.UserReadDto;
import microstamp.authorization.service.AnalysisService;
import microstamp.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private AnalysisService analysisService;

    public UserReadDto getMe(Jwt jwt) {
        String username = jwt.getSubject();
        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        return new UserReadDto(userId,
                username,
                analysisService.findByUserId(userId));
    }
}
