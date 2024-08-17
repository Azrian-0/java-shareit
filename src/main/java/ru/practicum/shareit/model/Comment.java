package ru.practicum.shareit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "created", updatable = false)
    private LocalDateTime created = LocalDateTime.now();
}
