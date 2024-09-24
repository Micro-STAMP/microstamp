package microstamp.authorization.mapper;

import microstamp.authorization.dto.UserReadDto;
import microstamp.authorization.entity.User;

public class UserMapper {

    public static UserReadDto toDto(User user) {
        return UserReadDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    public static User toEntity(String username, String encryptedPassword) {
        return new User(username, encryptedPassword);
    }
}
