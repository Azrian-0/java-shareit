package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;
import java.util.Optional;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemRequestDto itemRequestDto) {
        return patch(String.format("/%d", itemId), userId, itemRequestDto);
    }

    public ResponseEntity<Object> getItemById(Long itemId) {
        return get(String.format("/%d", itemId));
    }

    public ResponseEntity<Object> getItemsByUser(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItems(Optional<String> text, Long userId) {
        Map<String, Object> parameters = Map.of(
                "text", text.orElse("")
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> createComments(Long itemId, Long userId, CommentRequestDto commentRequestDto) {
        return post(String.format("/%d/comment", itemId), userId, commentRequestDto);
    }
}
