package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private Integer id = 1;

    @GetMapping
    public List<Film> getAllFilms() {

        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {

        checkReleaseDate(film);
        film.setId(getNextId());
        films.put(film.getId(),film);

        log.info("Film created {}", film);

        return film;
    }

    @PutMapping
    public Film updateUser(@Valid @RequestBody Film film) throws ValidationException {
        checkReleaseDate(film);

        if (films.containsKey(film.getId())) {
            log.info("Film information has been updated {}", film);
            films.put(film.getId(), film);
        } else {
            log.warn("Film with id = {} not found!", film.getId());
            throw new NotFoundException("Film not found!");
        }

        return film;
    }

    private void checkReleaseDate(Film film) throws ValidationException {
        if (!LocalDate.of(1895,12,28)
                        .isBefore(film.getReleaseDate())) {
            throw new ValidationException("Дата выхода фильма не может быть раньше чем 1895-12-28");
        }
    }

    private Integer getNextId() {
        return id++;
    }


}
