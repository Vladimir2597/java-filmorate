package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    @GetMapping
    public List<User> getAllUsers() {

        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {

        checkNameForNull(user);
        user.setId(getNextId());
        users.put(user.getId(),user);

        log.info("User created {}", user);

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        checkNameForNull(user);
        if (users.containsKey(user.getId())) {
            log.info("User information has been updated {}", user);
            users.put(user.getId(), user);
        } else {
            log.warn("User with id = {} not found!", user.getId());
            throw new NotFoundException("User not found!");
        }

        return user;
    }

    private void checkNameForNull(User user) {
        if (user.getName() == null ||
                user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private Integer getNextId() {

        return id++;
    }
}
