package ru.practicum.shareit.storage.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, String> emailInUse = new HashMap<>();

    private long userId = 0;

    @Override
    public User create(UserDto userDto) {
        if (userDto.getEmail() == null || !(userDto.getEmail().contains("@"))) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (emailInUse.containsValue(userDto.getEmail())) {
            throw new ConflictException("Почта " + userDto.getEmail() + " уже используется");
        }
        if (userDto.getName() == null) {
            throw new ConflictException("Имя не может быть пустым");
        }
        User user = new User(getNextId(), userDto.getName(), userDto.getEmail());
        users.put(user.getId(), user);
        emailInUse.put(user.getId(), user.getEmail());
        return user;
    }

    @Override
    public User update(UserDto userDto, Long userId) {
        User user = users.get(userId);
        if (userDto.getEmail() != null) {
            if (emailInUse.containsValue(userDto.getEmail())) {
                if (!userDto.getEmail().equals(emailInUse.get(userId))) {
                    throw new ConflictException("Почта " + userDto.getEmail() + " уже используется");
                }
            }
            if (userDto.getEmail().isBlank() || !(userDto.getEmail().contains("@"))) {
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            }
            emailInUse.remove(user.getId());
            user.setEmail(userDto.getEmail());
            emailInUse.put(user.getId(), user.getEmail());
        }
        if (userDto.getName() != null) {
            if (userDto.getName().isBlank()) {
                throw new ValidationException("Имя не может быть пустым");
            }
            user.setName(userDto.getName());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public User findById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователя с id - " + userId + ", не найден");
        }
        return users.get(userId);
    }

    @Override
    public void deleteById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователя с id - " + userId + ", не найден");
        }
        emailInUse.remove(userId);
        users.remove(userId);
    }

    @Override
    public Boolean isUserContains(Long userId) {
        return users.containsKey(userId);
    }

    private long getNextId() {
        return ++userId;
    }
}