package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> usersMap = new HashMap<>();
//    private int generatoreId = 0;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User getUser(int id) {
        return usersMap.get(id);
    }

    @Override
    public User createUser(User user) {
        usersMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
//        if (!usersMap.containsKey(user.getId())) {
//            throw new NotFoundException("Пользователь с идентификатором " +
//                    user.getId() + " не зарегистрирован!");
//        }
        usersMap.put(user.getId(), user);
        return user;
    }

    @Override
    public String deleteUser(User user) {
        if (!usersMap.containsKey(user.getId())) {
            throw new ValidationException("Пользователь " + user + " не может быть удален, так как еще не зарегестрирован");
        }
        usersMap.remove(user.getId());
        return "Пользователь с ID " + user.getId() + " удален.";
    }

    @Override
    public void addFriend(int idUser, int friendId) {
        User user = usersMap.get(idUser);
        User friend = usersMap.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(idUser);
        updateUser(user);
        updateUser(friend);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = usersMap.get(userId);
        user.deleteFriend(friendId);
        updateUser(user);
    }
}
