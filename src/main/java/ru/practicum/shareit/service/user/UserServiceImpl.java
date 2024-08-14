package ru.practicum.shareit.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage inMemoryUserStorage;

    @Override
    public User create(UserDto userDto) {
        return inMemoryUserStorage.create(userDto);
    }

    @Override
    public User update(UserDto userDto, Long userId) {
        return inMemoryUserStorage.update(userDto, userId);
    }

    @Override
    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @Override
    public User findById(Long userId) {
        return inMemoryUserStorage.findById(userId);
    }

    @Override
    public void deleteById(Long userId) {
        inMemoryUserStorage.deleteById(userId);
    }
}