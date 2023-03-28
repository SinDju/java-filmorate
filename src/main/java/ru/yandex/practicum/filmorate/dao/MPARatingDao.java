package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

public interface MPARatingDao {
    public List<MPARating> getAllMpa();
    public MPARating getMpaById(int id);
}
