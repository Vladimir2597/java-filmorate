package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {
    @EqualsAndHashCode.Include
    private long id;
    @NotBlank(message = "Film name cannot be blank")
    private String name;
    @Size(max = 200, message = "Maximum description size 200 characters")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Min(value = 1, message = "Film duration must be greater than zero")
    private Integer duration;
    private Set<Long> likes = new HashSet<>();

    public Set<Long> getLikes() {
        return likes;
    }

    public void putLike(long userId) {
        likes.add(userId);
    }

    public void deleteLike(long userId) {
        likes.remove(userId);
    }


}
