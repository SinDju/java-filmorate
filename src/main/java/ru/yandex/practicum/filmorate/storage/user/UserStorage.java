package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User getUser(int id);
    public List<User> getAllUsers();
    public User createUser(User user);
    public User updateUser(User user);
    public String deleteUser(User user);

    void addFriend(int idUser, int friendId);

    public void deleteFriend(int userId, int friendId);
}
