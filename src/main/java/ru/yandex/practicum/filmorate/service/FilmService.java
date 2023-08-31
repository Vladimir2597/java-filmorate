package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll() {

        return filmStorage.getAll();
    }

    public Film getById(long filmId) {

        return filmStorage.findById(filmId);
    }

    public List<Film> getPopular(int count) {

        return filmStorage.getPopular(count);
    }

    public Film create(Film film) throws ValidationException {
        checkReleaseDate(film);

        return filmStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {
        checkReleaseDate(film);

        return filmStorage.update(film);
    }

    public Film putLike(long filmId, long userId) throws ValidationException {

        return filmStorage.putLike(filmId,userId);
    }

    public Film deleteLike(long filmId, long userId) throws ValidationException {

        return filmStorage.deleteLike(filmId,userId);
    }


    private void checkReleaseDate(Film film) throws ValidationException {
        if (!LocalDate.of(1895, 12, 28)
                .isBefore(film.getReleaseDate())) {
            throw new ValidationException("The release date of the film cannot be earlier than 1895-12-28");
        }
    }

}
