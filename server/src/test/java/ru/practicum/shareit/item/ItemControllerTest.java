package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    protected void create_whenInvoked_thenReturnItemDto() {
        long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("name");
        Item item = new Item();
        item.setName("name");
        item.setOwner(new User());
        ItemDto expectedDto = new ItemDto();
        expectedDto.setName("name");
        expectedDto.setComments(Collections.emptyList());
        Mockito.when(itemService.create(userId, itemRequestDto)).thenReturn(item);

        ItemDto request = itemController.create(userId, itemRequestDto);

        Assertions.assertEquals(expectedDto, request);
    }

    @Test
    protected void update_whenInvoked_thenReturnItemDto() {
        long userId = 1L;
        long itemId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("name");
        Item item = new Item();
        item.setName("name");
        item.setOwner(new User());
        ItemDto expectedDto = new ItemDto();
        expectedDto.setName("name");
        expectedDto.setComments(Collections.emptyList());
        Mockito.when(itemService.update(userId, itemId, itemRequestDto)).thenReturn(item);

        ItemDto response = itemController.update(userId, itemId, itemRequestDto);

        Assertions.assertEquals(expectedDto, response);
    }

    @Test
    protected void getItemById_whenInvoked_thenReturnItemDto() {
        long itemId = 1L;
        Item item = new Item();
        item.setName("name");
        item.setOwner(new User());
        ItemDto expectedDto = new ItemDto();
        expectedDto.setName("name");
        expectedDto.setComments(Collections.emptyList());
        Mockito.when(itemService.getById(itemId)).thenReturn(item);

        ItemDto response = itemController.getById(itemId);

        Assertions.assertEquals(expectedDto, response);
    }

    @Test
    protected void getItemsByUser_whenInvoked_thenReturnItemDto() {
        long userId = 1L;
        Item item = new Item();
        item.setName("name");
        item.setOwner(new User());
        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setComments(Collections.emptyList());
        List<Item> items = List.of(item);
        List<ItemDto> expectedDto = List.of(itemDto);
        Mockito.when(itemService.getItemsByUser(userId)).thenReturn(items);

        List<ItemDto> response = itemController.getItemsByUser(userId);

        Assertions.assertEquals(expectedDto, response);
    }

    @Test
    protected void searchItems_whenInvoked_thenReturnItemDto() {
        long userId = 1L;
        String searchText = "searchText";
        Item item = new Item();
        item.setName("itemName");
        item.setOwner(new User());
        ItemDto itemDto = new ItemDto();
        itemDto.setName("itemName");
        itemDto.setComments(Collections.emptyList());
        List<Item> items = List.of(item);
        List<ItemDto> expectedDto = List.of(itemDto);
        Mockito.when(itemService.search(Optional.of(searchText), userId)).thenReturn(items);

        List<ItemDto> response = itemController.searchItems(Optional.of(searchText), userId);

        Assertions.assertEquals(expectedDto, response);
    }

    @Test
    protected void createComments_whenInvoked_thenReturnCommentDto() {
        long itemId = 1L;
        long userId = 1L;
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("commentText");
        Comment comment = new Comment();
        comment.setAuthor(new User());
        comment.setItem(new Item());
        comment.setText("commentText");
        CommentDto expectedDto = new CommentDto();
        expectedDto.setText("commentText");
        Mockito.when(itemService.createComment(commentRequestDto, userId, itemId)).thenReturn(comment);

        CommentDto response = itemController.createComments(itemId, userId, commentRequestDto);

        Assertions.assertEquals(expectedDto.getText(), response.getText());
    }
}
