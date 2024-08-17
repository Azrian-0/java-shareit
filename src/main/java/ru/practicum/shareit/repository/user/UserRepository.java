package ru.practicum.shareit.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
