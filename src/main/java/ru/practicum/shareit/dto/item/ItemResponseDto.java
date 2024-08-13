package ru.practicum.shareit.dto.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ItemResponseDto extends ItemDto {

    private Long id;
}
