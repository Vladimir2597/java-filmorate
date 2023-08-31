package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film create(Film film) throws ValidationException;

    Film update(Film film) throws ValidationException;

    Film findById(long userId);

    Film putLike(long filmId, long userId) throws ValidationException;

    Film deleteLike(long filmId, long userId) throws ValidationException;

    List<Film> getPopular(int count);

}
