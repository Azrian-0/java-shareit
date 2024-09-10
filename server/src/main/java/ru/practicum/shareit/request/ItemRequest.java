package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requests")
@NoArgsConstructor
@Getter
@Setter
public class ItemRequest {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "created", nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "request")
    private List<Item> items = new ArrayList<>();
}
