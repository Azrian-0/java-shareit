package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

@Data
public class UserRequestDto {

    @NotBlank(groups = {Create.class})
    @Size(groups = {Create.class, Update.class}, max = 30)
    private String name;

    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    @Size(groups = {Create.class, Update.class}, max = 30)
    private String email;
}
