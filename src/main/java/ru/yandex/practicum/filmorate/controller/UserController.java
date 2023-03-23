package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Получен запрос GET к эндпоинту: /users");
        return userService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST. Данные тела запроса: {}", user);
        User createUser = userService.addUser(user);
        log.info("Создан объект {} с идентификатором {}", User.class.getSimpleName(), createUser.getId());
        return createUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", user);
        User updateUser = userService.updateUser(user);
        log.info("Обновлен объект {} с идентификатором {}", User.class.getSimpleName(), updateUser.getId());
        return updateUser;
    }

    @GetMapping ("/{id}") // получения юзера по id
    public User findById(@PathVariable String id) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/", id);
        return userService.findById(id);
    }

    @PutMapping("/{id}/friends/{friendId}") // добавление в друзья
    public void addFriends(@PathVariable String id, @PathVariable String friendId) {
        log.info("Получен запрос PUT к эндпоинту: /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") // удаление из друзей
    public void deleteFriends(@PathVariable String id, @PathVariable String friendId) {
        log.info("Получен запрос DELETE к эндпоинту: /users/{}/friends/{}", id, friendId);
        userService.deleteUserFriend(id, friendId);
        log.info("Обновлен объект {} с идентификатором {}. Удален друг {}",
                User.class.getSimpleName(), id, friendId);
    }

    @GetMapping("/{id}/friends") // получения списка друзей юзера по id
    public List<User> getUserFriends(@PathVariable String id) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/", id);
        return  userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") // список друзей, общих с другим пользователем
    public Collection<User> getUserСommonFriends(@PathVariable String id, @PathVariable String otherId) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/friends/common/{}", id, otherId);
        return  userService.getCommonFriends(id, otherId);
    }
}
