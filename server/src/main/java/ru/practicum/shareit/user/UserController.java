package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.mappers.UserMapper;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
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
    public String deleteById(@PathVariable Long userId) {
        log.info("DELETE /users/{}", userId);
        return String.format("{\"message\":\"%s\"}", userService.deleteById(userId));
    }
}
