package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserResponseDto;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.service.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody UserDto userDto) {
        log.info("Обработан POST user запрос. email - {}", userDto.getEmail());
        return UserMapper.map(userService.create(userDto));
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update(@Valid @RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Обработан PATCH /users/{} запрос.", userId);
        return UserMapper.map(userService.update(userDto, userId));
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        log.info("Обработан GET users запрос.");
        return userService.findAll().stream().map(UserMapper::map).collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserResponseDto findById(@PathVariable Long userId) {
        log.info("Обработан GET /users/{} запрос.", userId);
        return UserMapper.map(userService.findById(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("Обработан DELETE /users/{} запрос.", userId);
        userService.deleteById(userId);
    }
}