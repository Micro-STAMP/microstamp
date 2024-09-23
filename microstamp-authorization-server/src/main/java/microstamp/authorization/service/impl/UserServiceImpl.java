package microstamp.authorization.service.impl;

import lombok.AllArgsConstructor;
import microstamp.authorization.dto.UserInsertDto;
import microstamp.authorization.dto.UserReadDto;
import microstamp.authorization.entity.User;
import microstamp.authorization.exception.UserAlreadyExistException;
import microstamp.authorization.mapper.UserMapper;
import microstamp.authorization.repository.UserRepository;
import microstamp.authorization.service.AnalysisService;
import microstamp.authorization.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AnalysisService analysisService;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserReadDto getMe(Jwt jwt) {
        String username = jwt.getSubject();
        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        return new UserReadDto(userId,
                username,
                analysisService.findByUserId(userId));
    }

    public UserReadDto insert(UserInsertDto userInsertDto) {
        User existingUser = userRepository.findByUsername(userInsertDto.getUsername());

        if (existingUser != null)
            throw new UserAlreadyExistException();

        String encryptedPassword = passwordEncoder.encode(userInsertDto.getPassword());
        User user = UserMapper.toEntity(userInsertDto.getUsername(), encryptedPassword);

        userRepository.save(user);

        return UserMapper.toDto(user);
    }
}
