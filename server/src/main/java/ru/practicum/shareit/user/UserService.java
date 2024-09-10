package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserRequestDto;

import java.util.List;

public interface UserService {

    User create(UserRequestDto userRequestDto);

    User update(UserRequestDto userRequestDto, Long userId);

    List<User> findAll();

    User findById(Long userId);

    String deleteById(Long userId);
}
