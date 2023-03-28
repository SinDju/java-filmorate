package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.impl.MPARatingDbStorage;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class RatingMPAController {
    private final MPARatingDbStorage mpaDbStorage;

    @Autowired
    public RatingMPAController(MPARatingDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    @GetMapping
    public List<MPARating> getAllMpa() {
        log.info("Получен запрос GET к эндпоинту: /mpa");
        return mpaDbStorage.getAllMpa();
    }

    @GetMapping("/{id}")
    public MPARating getMpaByID(@PathVariable int id) {
        log.info("Получен запрос GET к эндпоинту: /mpa/{id}");
        return mpaDbStorage.getMpaById(id);
    }
}

