package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetExceptions;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class PostService {
    private final UserService userService;

    private final Map<Long, Post> posts = new HashMap<>();
    private final Comparator<Post> postDateComporator = Comparator.comparing(Post::getPostDate);

    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(SortOrder sort, int from, int size) {
        return posts.values()
                .stream()
                .sorted(sort.equals(SortOrder.ASCENDING) ?
                        postDateComporator : postDateComporator.reversed())
                .skip(from)
                .limit(size)
                .toList();
    }

    public Post findPostById(long id) {
        return posts.get(id);
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetExceptions("Описание не может быть пустым");
        }

        if (!userService.findUserById(post.getAuthorId()).isPresent()) {
            throw new ConditionsNotMetExceptions("Автор с id = " + post.getAuthorId() + " не найден");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetExceptions("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetExceptions("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
