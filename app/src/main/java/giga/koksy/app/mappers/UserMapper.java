package giga.koksy.app.mappers;

import giga.koksy.app.dto.UserDto;
import giga.koksy.app.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto map(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());

        return userDto;
    }

    public static List<UserDto> mapUsersDto(List<User> users) {
        return users.stream().map(UserMapper::map).collect(Collectors.toList());
    }

    public static User map(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        return user;
    }

    public static List<User> mapUsers(List<UserDto> usersDto) {
        return usersDto.stream().map(UserMapper::map).collect(Collectors.toList());
    }
}
