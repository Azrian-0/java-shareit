package ru.practicum.shareit.storage.user;

import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.model.User;

import java.util.List;

public interface UserStorage {

    User create(UserDto userDto);

    User update(UserDto userDto, Long userId);

    List<User> findAll();

    User findById(Long userId);

    void deleteById(Long userId);

    Boolean isUserContains(Long userId);
}