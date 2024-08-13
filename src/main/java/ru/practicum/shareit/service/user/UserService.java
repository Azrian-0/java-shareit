package ru.practicum.shareit.service.user;

import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.model.User;

import java.util.List;

public interface UserService {

    User create(UserDto userDto);

    User update(UserDto userDto, Long userId);

    List<User> findAll();

    User findById(Long userId);

    void deleteById(Long userId);
}