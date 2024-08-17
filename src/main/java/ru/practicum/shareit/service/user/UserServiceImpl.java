package ru.practicum.shareit.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.user.UserRequestDto;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.user.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User create(UserRequestDto userRequestDto) {
        if (userRequestDto.getEmail() == null || !(userRequestDto.getEmail().contains("@"))) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (userRequestDto.getName() == null) {
            log.error("Имя не может быть пустым");
            throw new ConflictException("Имя не может быть пустым");
        }
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(UserRequestDto userRequestDto, Long userId) {
        User user;
        if (!userRepository.existsById(userId)) {
            log.error(String.format("Пользователь с id - %d, не найден", userId));
            throw new NotFoundException(String.format("Пользователь с id - %d, не найден", userId));
        } else {
            user = userRepository.findById(userId).get();
        }
        if (userRequestDto.getEmail() != null) {
            if (userRequestDto.getEmail().isBlank() || !(userRequestDto.getEmail().contains("@"))) {
                log.error("Электронная почта не может быть пустой и должна содержать символ @");
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            }
            user.setEmail(userRequestDto.getEmail());
        }
        if (userRequestDto.getName() != null) {
            if (userRequestDto.getName().isBlank()) {
                log.error("Имя не может быть пустым");
                throw new ValidationException("Имя не может быть пустым");
            }
            user.setName(userRequestDto.getName());
        }
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.error(String.format("Почта %s уже существует, укажите другую", user.getEmail()));
            throw new ConflictException(String.format("Почта %s уже существует, укажите другую", user.getEmail()));
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error(String.format("Пользователь с id - %d, не найден", userId));
            throw new NotFoundException(String.format("Пользователь с id - %d, не найден", userId));
        }
        return userRepository.findById(userId).get();
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error(String.format("Пользователь с id - %d, не найден", userId));
            throw new NotFoundException(String.format("Пользователь с id - %d, не найден", userId));
        }
        userRepository.deleteById(userId);
    }
}