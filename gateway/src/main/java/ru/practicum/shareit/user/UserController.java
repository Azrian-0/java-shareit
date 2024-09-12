package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

@Controller
@RequestMapping(path = "/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    @Validated(Create.class)
    public ResponseEntity<Object> create(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("Обработан POST user запрос.");
        return userClient.create(userRequestDto);
    }

    @PatchMapping("/{userId}")
    @Validated(Update.class)
    public ResponseEntity<Object> update(@Valid @RequestBody UserRequestDto userRequestDto, @PathVariable Long userId) {
        log.info("Обработан PATCH /users/{} запрос.", userId);
        return userClient.update(userRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Обработан GET users запрос.");
        return userClient.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUserById(@PathVariable Long userId) {
        log.info("Обработан GET /users/{} запрос.", userId);
        return userClient.findUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long userId) {
        log.info("Обработан DELETE /users/{} запрос.", userId);
        return userClient.deleteUserById(userId);
    }
}
