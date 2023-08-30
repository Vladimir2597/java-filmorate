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
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {

        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long filmId) {

        return filmStorage.findFilmById(filmId);
    }

    public List<Film> getPopularFilms(int count) {

        return filmStorage.getAllFilms()
                .stream()
                .sorted((f0, f1) -> compare(f0, f1))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film createFilm(Film film) throws ValidationException {
        checkReleaseDate(film);

        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        checkReleaseDate(film);

        return filmStorage.updateFilm(film);
    }

    public Film putLike(long filmId, long userId) throws ValidationException {
        Film film = filmStorage.findFilmById(filmId);

        userStorage.findUserById(userId); // check that user exists

        film.putLike(userId);

        return filmStorage.updateFilm(film);
    }

    public Film deleteLike(long filmId, long userId) throws ValidationException {
        Film film = filmStorage.findFilmById(filmId);

        userStorage.findUserById(userId); // check that user exists

        film.deleteLike(userId);

        return filmStorage.updateFilm(film);
    }

    private int compare(Film f0, Film f1) {
        int result = Integer.compare(f0.getLikes().size(),
                f1.getLikes().size());
        return -result;
    }

    private void checkReleaseDate(Film film) throws ValidationException {
        if (!LocalDate.of(1895, 12, 28)
                .isBefore(film.getReleaseDate())) {
            throw new ValidationException("The release date of the film cannot be earlier than 1895-12-28");
        }
    }

}
