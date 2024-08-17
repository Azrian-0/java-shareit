package ru.practicum.shareit.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
