package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final UserStorage userStorage;

    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1;

    @Autowired
    public InMemoryFilmStorage(UserStorage userStorage) {

        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getAll() {

        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) throws ValidationException {
        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Film created {}", film);

        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {

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
    public Film findById(long filmId) {
        return films.entrySet().stream()
                .filter(f -> f.getKey() == filmId)
                .map(f -> f.getValue())
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException("Film with id: " + filmId + " not found")
                );
    }

    @Override
    public Film putLike(long filmId, long userId) throws ValidationException {
        Film film = findById(filmId);

        userStorage.findById(userId); // check that user exists

        film.putLike(userId);

        return update(film);
    }

    @Override
    public Film deleteLike(long filmId, long userId) throws ValidationException {
        Film film = findById(filmId);

        userStorage.findById(userId); // check that user exists

        film.deleteLike(userId);

        return update(film);
    }

    @Override
    public List<Film> getPopular(int count) {

        return getAll()
                .stream()
                .sorted((f0, f1) -> compare(f0, f1))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        int result = Integer.compare(f0.getLikes().size(),
                f1.getLikes().size());
        return -result;
    }

    private long getNextId() {
        return id++;
    }
}
