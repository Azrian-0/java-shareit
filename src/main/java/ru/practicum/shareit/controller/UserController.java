package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserRequestDto;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.service.user.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("Обработан POST user запрос. email - {}", userRequestDto.getEmail());
        return UserMapper.map(userService.create(userRequestDto));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Valid @RequestBody UserRequestDto userRequestDto, @PathVariable Long userId) {
        log.info("Обработан PATCH /users/{} запрос.", userId);
        return UserMapper.map(userService.update(userRequestDto, userId));
    }

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Обработан GET users запрос.");
        return userService.findAll().stream().map(UserMapper::map).toList();
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable Long userId) {
        log.info("Обработан GET /users/{} запрос.", userId);
        return UserMapper.map(userService.findById(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("Обработан DELETE /users/{} запрос.", userId);
        userService.deleteById(userId);
    }

}