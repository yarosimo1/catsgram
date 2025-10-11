package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"postId", "originalFileName", "filePath"})
public class Image {
    private Long id;
    private long postId;
    private String originalFileName;
    private String filePath;
}
