package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
        private HashMap<Integer, User> usersMap = new HashMap<>();
        private int generatoreId = 0;

        @GetMapping
        public List<User> getAllUsers() {
            return new ArrayList<>(usersMap.values());
        }

        @PostMapping
        public User createUser(@Valid @RequestBody User user) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            final int id = ++generatoreId;
            user.setId(id);
            usersMap.put(id, user);
            return user;
        }

        @PutMapping
        public User updateUser(@Valid @RequestBody User user) {
            if (usersMap.containsKey(user.getId())){
                usersMap.put(user.getId(), user);
            } else {
                throw new ValidationException("Пользователь еще не зарегестрирован");
            }
            return user;
        }
    }
