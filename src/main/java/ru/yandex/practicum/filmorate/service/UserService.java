package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ErrorIdException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    // Сервис реализует основную логику проверки и запрашивает необходимую информацию от Стореджа
    private UserStorage userStorage;
    private int generatoreId = 0;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User findById(String id) {
        return getUserStorage(id);
    }


    public List<User> getAll() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        getUserStorage(user.getId().toString());
        return userStorage.updateUser(user);
    }

    private int stringForInt(String id) {
        try {
            return Integer.valueOf(id);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    // получение списка общих друзей двух пользователей
    public Collection<User> getCommonFriends(String userId, String friendId) {
        User user = getUserStorage(userId);
        User userFriend = getUserStorage(friendId);
        Collection<User> commonFriends = new HashSet<>();
        for (Integer idFriend : user.getFriendIds()) {
            if (userFriend.getFriendIds().contains(idFriend)) {
                commonFriends.add(userStorage.getUser(idFriend).get());
            }
        }
        return commonFriends;
    }

    // получение списка друзей пользователя
    public List<User> getFriends(String userId) {
        User user = getUserStorage(userId);
        ArrayList<User> friendsUser = new ArrayList<>();
        for (Integer idFriend : user.getFriendIds()) {
            friendsUser.add(userStorage.getUser(idFriend).get());
        }
        return friendsUser;
    }

    public void deleteUserFriend(String userId, String friendId) {
        User user = getUserStorage(userId);
        User userFriend = getUserStorage(friendId);
        userStorage.deleteFriend(user.getId(), userFriend.getId());
    }

    public void addFriend(String userId, String friendId) {
        User user = getUserStorage(userId);
        User friendUser = getUserStorage(friendId);
        userStorage.addFriend(user.getId(), friendUser.getId());
    }

    public User getUserStorage(String supposedId) {
        Integer userId = stringForInt(supposedId);
        if(userId == Integer.MIN_VALUE) {
            throw new ErrorIdException("Не удалось распознать идентификатор пользователя: "
                    + supposedId);
        }
       User user = userStorage.getUser(userId).orElseThrow(() ->
             new NotFoundException("Пользователь с ID " +
                    supposedId + " не зарегистрирован!"));
        return user;
    }
}
