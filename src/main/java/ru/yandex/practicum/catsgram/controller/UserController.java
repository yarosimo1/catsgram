package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.getUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.postUser(user);
    }

    @PutMapping
    public User udate(@RequestBody User user) {
        return userService.putUser(user);
    }
}
