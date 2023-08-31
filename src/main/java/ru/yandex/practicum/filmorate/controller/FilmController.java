package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {

        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {

        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(required = false) Optional<Integer> count) {
        int defaultNumberSort = 10;

        return filmService.getPopular(count
                .orElse(defaultNumberSort));
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {

        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {

        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLike(@PathVariable long id, @PathVariable long userId) throws ValidationException {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) throws ValidationException {
        return filmService.deleteLike(id, userId);
    }

}
