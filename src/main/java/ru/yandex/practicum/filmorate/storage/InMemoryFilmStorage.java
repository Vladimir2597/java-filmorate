package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1;

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) throws ValidationException {
        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Film created {}", film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {

        if (films.containsKey(film.getId())) {
            log.info("Film information has been updated {}", film);
            films.put(film.getId(), film);
        } else {
            log.warn("Film with id = {} not found!", film.getId());
            throw new NotFoundException("Film not found!");
        }

        return film;
    }

    @Override
    public Film findFilmById(long filmId) {
        return films.entrySet().stream()
                .filter(f -> f.getKey() == filmId)
                .map(f -> f.getValue())
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException("Film with id: " + filmId + " not found")
                );
    }

    private long getNextId() {
        return id++;
    }
}
