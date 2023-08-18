package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public Film createFilm(@Valid @RequestBody Film film){

        checkReleaseDate(film);
        film.setId(getNextId());
        films.put(film.getId(),film);

        return film;
    }

    @PutMapping
    public Film updateUser(@Valid @RequestBody Film film){
        checkReleaseDate(film);

        if(films.containsKey(film.getId())){
            films.put(film.getId(), film);
        }

        return film;
    }

    private void checkReleaseDate(Film film){
        if(LocalDate.of(1895,12,28)
                        .isBefore(film.getReleaseDate())){
            throw new ValidationException("Invalid release date!");
        }
    }

    private Integer getNextId(){
        return id ++;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

}
