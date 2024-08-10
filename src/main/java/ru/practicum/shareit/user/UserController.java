package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserInterface userService;

    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {
        log.info("Обработан POST user запрос. email - {}", userDto.getEmail());
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public User update(@Valid @RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Обработан PATCH /users/{} запрос.", userId);
        return userService.update(userDto, userId);
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Обработан GET users запрос.");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable Long userId) {
        log.info("Обработан GET /users/{} запрос.", userId);
        return userService.findById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("Обработан DELETE /users/{} запрос.", userId);
        userService.deleteById(userId);
    }
}