package ru.practicum.shareit.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    private String email;
}