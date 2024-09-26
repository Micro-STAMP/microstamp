package microstamp.authorization.unit;

import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.ImageReadDto;
import microstamp.authorization.dto.UserInsertDto;
import microstamp.authorization.dto.UserReadDto;
import microstamp.authorization.entity.User;
import microstamp.authorization.exception.DistinctPasswordException;
import microstamp.authorization.exception.UserAlreadyExistException;
import microstamp.authorization.repository.UserRepository;
import microstamp.authorization.service.AnalysisService;
import microstamp.authorization.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private AnalysisService analysisService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("#getMe > When the userId is not a valid UUID > Throw an exception")
    void getMeWhenTheUserIdIsNotAValidUuidThrowAnException() {
        Jwt mock = assembleJwtWithInvalidUserId.get();

        assertThrows(IllegalArgumentException.class, () -> service.getMe(mock));
    }

    @Test
    @DisplayName("#getMe > When the jwt is valid > Return the user")
    void getMeWhenTheJwtIsValidReturnTheUser() {
        Jwt mock = assembleJwt.get();
        AnalysisReadDto mockAnalysis = assembleAnalysisRead.get();

        when(analysisService.findByUserId(UUID.fromString(mock.getClaim("userId")))).thenReturn(List.of(mockAnalysis));

        UserReadDto response = service.getMe(mock);

        assertAll(
                () -> assertEquals(UUID.fromString(mock.getClaim("userId")), response.getId()),
                () -> assertEquals(mock.getClaim("sub"), response.getUsername()),
                () -> assertEquals(mockAnalysis, response.getAnalyses().getFirst())
        );
    }

    @Test
    @DisplayName("#insert > When the user exists > Throw an exception")
    void insertWhenTheUserExistsThrowAnException() {
        UserInsertDto userInsert = assembleUserInsert.get();

        when(userRepository.findByUsername(userInsert.getUsername())).thenReturn(User.builder().build());

        assertThrows(UserAlreadyExistException.class, () -> service.insert(userInsert));
    }

    @Test
    @DisplayName("#insert > When the password and the matching password mismatch > Throw an exception")
    void insertWhenThePasswordAndTheMatchingPasswordMismatchThrowAnException() {
        UserInsertDto userInsert = assembleUserInsert.get();
        userInsert.setMatchingPassword(userInsert.getPassword().substring(3));

        when(userRepository.findByUsername(userInsert.getUsername())).thenReturn(null);

        assertThrows(DistinctPasswordException.class, () -> service.insert(userInsert));
    }

    @Test
    @DisplayName("#insert > When a new user is created > Return the user")
    void insertWhenANewUserIsCreatedReturnTheUser() {
        UserInsertDto userInsert = assembleUserInsert.get();

        when(userRepository.findByUsername(userInsert.getUsername())).thenReturn(null);

        UserReadDto response = service.insert(userInsert);

        assertEquals(userInsert.getUsername(), response.getUsername());
    }

    private final Supplier<Jwt> assembleJwt = () -> new Jwt(
            "token",
            Instant.now(),
            Instant.now().plusSeconds(3600),
            new HashMap<>() {{
                put("header", "example");
            }},
            new HashMap<>() {{
                put("userId", UUID.randomUUID().toString());
                put("sub", "subject");
            }});

    private final Supplier<Jwt> assembleJwtWithInvalidUserId = () -> new Jwt(
            "token",
            Instant.now(),
            Instant.now().plusSeconds(3600),
            new HashMap<>() {{
                put("header", "example");
            }},
            new HashMap<>() {{
                put("userId", "abc");
                put("sub", "subject");
            }});

    private final Supplier<AnalysisReadDto> assembleAnalysisRead = () -> AnalysisReadDto.builder()
            .id(UUID.randomUUID())
            .name("Mock analysis")
            .creationDate(Instant.now())
            .userId(UUID.randomUUID())
            .image(ImageReadDto.builder()
                    .id(UUID.randomUUID())
                    .fileName("image.png")
                    .base64("mockBase64")
                    .build())
            .build();

    private final Supplier<UserInsertDto> assembleUserInsert = () -> UserInsertDto.builder()
            .username("microstamp")
            .password("pass123")
            .matchingPassword("pass123")
            .build();
}
