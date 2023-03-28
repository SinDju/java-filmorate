package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MPARatingDao;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.ArrayList;
import java.util.List;

@Component
public class MPARatingDbStorage implements MPARatingDao {
    private final JdbcTemplate jdbcTemplate;

    public MPARatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPARating> getAllMpa() {
        List<MPARating> mpaList = new ArrayList<>();
        String sql = "SELECT * FROM MPARATING";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            MPARating MPARating = new MPARating(sqlRowSet.getInt("ID"),
                    sqlRowSet.getString("RATING_NAME"));
            mpaList.add(MPARating);
        }
        return mpaList;
    }

    @Override
    public MPARating getMpaById(int id) {
        String sql = "SELECT * FROM MPARATING WHERE ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (!sqlRowSet.next()) {
            throw new NotFoundException("Такого рейтенга нет");
        }
        return new MPARating(sqlRowSet.getInt("ID"),
                sqlRowSet.getString("RATING_NAME"));
    }
}
