package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoTest {
    private final UserDbStorage userStorage;

    @Order(1)
    @Test
    public void testGetAllUsers() throws NotFoundException {
        userStorage.createUser(User.builder().login("Test").birthday(LocalDate.now()).name("TEST").email("test@ya.ru").build());
        userStorage.createUser(User.builder().login("Test1").birthday(LocalDate.now()).name("TEST1").email("test1@ya.ru").build());
        userStorage.createUser(User.builder().login("Test2").birthday(LocalDate.now()).name("TEST2").email("test2@ya.ru").build());
        List<User> users = userStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(3);
        assertThat(users.get(0).getLogin()).isEqualTo("Test");
        assertThat(users.get(0).getName()).isEqualTo("TEST");
        assertThat(users.get(0).getBirthday()).isNotNull();
        assertThat(users.get(0).getEmail()).isEqualTo("test@ya.ru");
        assertThat(users.get(1).getLogin()).isEqualTo("Test1");
        assertThat(users.get(2).getId()).isEqualTo(3);
    }

    @Order(2)
    @Test
    public void testFindUserById() throws NotFoundException {
        userStorage.createUser(User.builder().login("Test").birthday(LocalDate.now()).name("TEST").email("test@ya.ru").build());
        User user1 = userStorage.getUser(1).get();
        assertThat(user1).hasFieldOrPropertyWithValue("id", 1);
        assertThat(user1.getLogin()).isEqualTo("Test");
        assertThat(user1.getBirthday()).isNotNull();
        assertThat(user1.getName()).isEqualTo("TEST");
        assertThat(user1.getEmail()).isEqualTo("test@ya.ru");
    }

    @Order(3)
    @Test
    public void testUserUpdate() throws  NotFoundException {
        userStorage.createUser(User.builder().login("Test").birthday(LocalDate.now()).name("TEST").email("test@ya.ru").build());
        User user = userStorage.getUser(1).get();
        user.setLogin("TEST1");
        userStorage.updateUser(user);
        assertThat(userStorage.getUser(1).get().getLogin()).isEqualTo("TEST1");
    }

    @Order(4)
    @Test
    void add_And_DeleteFriend() throws NotFoundException {
        userStorage.createUser(User.builder().login("Test").birthday(LocalDate.now()).name("TEST").email("test@ya.ru").build());
        userStorage.createUser(User.builder().login("Test1").birthday(LocalDate.now()).name("TEST1").email("test1@ya.ru").build());
        userStorage.createUser(User.builder().login("Test2").birthday(LocalDate.now()).name("TEST2").email("test2@ya.ru").build());
        assertThat(userStorage.getUser(1).get().getFriendIds().size()).isEqualTo(0);
        assertThat(userStorage.getUser(2).get().getFriendIds().size()).isEqualTo(0);
        userStorage.addFriend(1, 2);
        assertThat(userStorage.getUser(1).get().getFriendIds().size()).isEqualTo(1);
        assertThat(userStorage.getUser(2).get().getFriendIds().size()).isEqualTo(0);
        userStorage.addFriend(2, 1);
        assertThat(userStorage.getUser(1).get().getFriendIds().size()).isEqualTo(1);
        assertThat(userStorage.getUser(2).get().getFriendIds().size()).isEqualTo(1);
        userStorage.deleteFriend(2, 1);
        assertThat(userStorage.getUser(1).get().getFriendIds().size()).isEqualTo(1);
        assertThat(userStorage.getUser(2).get().getFriendIds().size()).isEqualTo(0);
        userStorage.deleteFriend(1, 2);
        assertThat(userStorage.getUser(1).get().getFriendIds().size()).isEqualTo(0);
        assertThat(userStorage.getUser(2).get().getFriendIds().size()).isEqualTo(0);
    }
}
