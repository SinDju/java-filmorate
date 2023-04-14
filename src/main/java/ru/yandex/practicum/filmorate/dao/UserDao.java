package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserDao {
    List<Integer> getUserFriends(int userId);
    // получение списка друзей пользователя
    //List<User> getFriends(String userId);

    // получение списка общих друзей двух пользователей
    //Collection<User> getCommonFriends(String userId, String friendId);
}
