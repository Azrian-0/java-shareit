package ru.practicum.shareit.service.user;

import ru.practicum.shareit.dto.user.UserRequestDto;
import ru.practicum.shareit.model.User;

import java.util.List;

public interface UserService {

    User create(UserRequestDto userRequestDto);

    User update(UserRequestDto userRequestDto, Long userId);

    List<User> findAll();

    User findById(Long userId);

    void deleteById(Long userId);
}