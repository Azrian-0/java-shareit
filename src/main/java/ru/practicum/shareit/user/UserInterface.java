package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserInterface {

    User create(UserDto userDto);

    User update(UserDto userDto, Long userId);

    List<User> findAll();

    User findById(Long userId);

    void deleteById(Long userId);
}