package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS").usingGeneratedKeyColumns("ID");
    }

    @Override
    public User createUser(User user) {
        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            user.setName(user.getLogin());
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("EMAIL", user.getEmail())
                .addValue("LOGIN", user.getLogin())
                .addValue("NAME", user.getName())
                .addValue("BIRTHDAY", user.getBirthday());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        if (!user.getFriendIds().isEmpty()) {
            for (Integer friendId : user.getFriendIds()) {
                addFriend(user.getId(), friendId);
            }
        }
        return getUser(user.getId()).get();
    }

    public List<Integer> getUserFriends(int userId) {
        String sqlGetFriends = "SELECT FRIEND_ID  FROM FRIENDS WHERE USER_ID = ?";
        return jdbcTemplate.queryForList(sqlGetFriends, Integer.class, userId);
    }

    @Override
    public void addFriend(int idUser, int friendId) {
        Boolean status = false;
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FRIENDS").usingGeneratedKeyColumns("ID");
        User friendUser = getUser(friendId).get();
        if (friendUser.getFriendIds().contains(idUser)) {
            status = true;
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("USER_ID", idUser)
                .addValue("FRIEND_ID", friendId)
                .addValue("ACCEPTED", status);
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
    }

    @Override
    public Optional<User> getUser(int id) {
        String sql = "SELECT * FROM USERS WHERE ID=?";
        SqlRowSet rowUser = jdbcTemplate.queryForRowSet(sql, id);
        if (rowUser.next()) {
            User user = User.builder().id(rowUser.getInt("ID"))
                    .email(rowUser.getString("EMAIL"))
                    .login(rowUser.getString("LOGIN"))
                    .name(rowUser.getString("NAME"))
                    .birthday(rowUser.getDate("BIRTHDAY").toLocalDate()).build();
            List<Integer> friendsId = getUserFriends(user.getId());
            if (friendsId != null) {
                for (Integer friendId : friendsId) {
                    user.addFriend(friendId);
                }
            }
            log.info("Найден пользователь: {} {} {} {} {} {}", user.getId(), user.getLogin(),
                    user.getName(), user.getEmail(), user.getBirthday(), user.getFriendIds());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> userRowMapper(rs));
    }

    private final User userRowMapper(ResultSet rs) throws SQLException {
        User user = User.builder().id(rs.getInt("ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate()).build();
        List<Integer> friendsId = getUserFriends(user.getId());
        if (friendsId != null) {
            for (Integer friendId : friendsId) {
                user.addFriend(friendId);
            }
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE USERS SET ID = ?, EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                "WHERE ID = ?";
        jdbcTemplate.update(sql, user.getId(), user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (!user.getFriendIds().isEmpty()) {
            for (Integer friendId : user.getFriendIds()) {
                addFriend(user.getId(), friendId);
            }
        }
        return user;
    }

    @Override
    public String deleteUser(User user) {
        String sql = "DELETE FROM USERS WHERE ID = ?";
        jdbcTemplate.update(sql, user.getId());
        return "Пользователь с ID " + user.getId() + " удален.";
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
        User user = getUser(userId).get();
        user.deleteFriend(friendId);
    }
}

