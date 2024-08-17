package ru.practicum.shareit.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserRequestDto {

    private String name;

    @Email
    private String email;
}
