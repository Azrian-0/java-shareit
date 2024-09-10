package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User create(UserRequestDto userRequestDto) {
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
            user.setEmail(userRequestDto.getEmail());
        }
        if (userRequestDto.getName() != null) {
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
    public String deleteById(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error(String.format("Пользователь с id - %d, не найден", userId));
            throw new NotFoundException(String.format("Пользователь с id - %d, не найден", userId));
        }
        userRepository.deleteById(userId);
        log.info("Пользователь с id - " + userId + ", удалил свой аккаунт");
        return "Пользователь с id - " + userId + ", удалил свой аккаунт";
    }
}
