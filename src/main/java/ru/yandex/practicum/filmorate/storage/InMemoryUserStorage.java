package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("User created {}", user);

        return user;
    }

    @Override
    public User updateUser(User user) {

        if (users.containsKey(user.getId())) {
            log.info("User information has been updated {}", user);
            users.put(user.getId(), user);
        } else {
            log.warn("User with id = {} not found!", user.getId());
            throw new NotFoundException("User not found!");
        }

        return user;
    }

    @Override
    public User findUserById(long userId) {
        return users.entrySet().stream()
                .filter(u -> u.getKey() == userId)
                .map(u -> u.getValue())
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException("User with id: " + userId + " not found")
                );
    }

    private long getNextId() {

        return id++;
    }
}
