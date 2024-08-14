package ru.practicum.shareit.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserResponseDto extends UserDto {

    private Long id;
}
