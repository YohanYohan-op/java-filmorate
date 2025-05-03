package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MPAController {

    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MPAController(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<MPA> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MPA getMpaById(@PathVariable int id) {
        return mpaDbStorage.getMpaById(id)
                .orElseThrow(() -> new MpaNotFoundException(id));
    }
}
