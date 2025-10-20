package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetExceptions;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        return users.values();
    }

    public User getUserById(long id) {
        return users.get(id);
    }

    public User postUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetExceptions("Имейл должен быть указан");
        }

        emailCheck(user);

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        users.put(user.getId(), user);
        return user;
    }

    public User putUser(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetExceptions("Id должен быть указан");
        }

        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }

        // Проверяем email
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            if (!oldUser.getEmail().equalsIgnoreCase(newUser.getEmail())) {
                emailCheck(newUser); // Проверка на дубликат
                oldUser.setEmail(newUser.getEmail());
            }
        }

        // Проверяем пароль
        if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
            oldUser.setPassword(newUser.getPassword());
        }

        // Проверяем имя
        if (newUser.getUsername() != null && !newUser.getUsername().isBlank()) {
            oldUser.setUsername(newUser.getUsername());
        }

        // Возвращаем обновлённого пользователя
        return oldUser;
    }


    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void emailCheck(User user) {
        users.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(user.getEmail()))
                .findAny()
                .ifPresent(u -> {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                });
    }

    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }
}
