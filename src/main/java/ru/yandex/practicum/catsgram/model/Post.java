package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(exclude = {"authorId", "description", "postDate"})
public class Post {
    private Long id;
    private long authorId;
    private String description;
    private Instant postDate;
}
