package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    private long id;
    @NotBlank
    @Email(message = "Email must be in correct format")
    private String email;
    @Pattern(regexp = "^\\S*$", message = "Login cannot contain spaces")
    private String login;
    private String name;
    @Past(message = "Birthday should be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public void addFriend(long friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(long friendId) {
        friends.remove(friendId);
    }
}
