package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("User created {}", user);

        return user;
    }

    @Override
    public User update(User user) {

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
    public User findById(long userId) {
        return users.entrySet().stream()
                .filter(u -> u.getKey() == userId)
                .map(u -> u.getValue())
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException("User with id: " + userId + " not found")
                );
    }

    @Override
    public User addFriend(long userId, long friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);

        update(friend);
        return update(user);
    }

    @Override
    public User deleteFriend(long userId, long friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        user.deleteFriend(friendId);
        friend.deleteFriend(userId);

        update(friend);
        return update(user);
    }

    @Override
    public List<User> getFriends(long userId) {

        return findById(userId)
                .getFriends().stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherUserId) {

        return findById(userId)
                .getFriends().stream()
                .filter(findById(otherUserId)
                        .getFriends()::contains)
                .map(this::findById)
                .collect(Collectors.toList());

    }

    private long getNextId() {

        return id++;
    }
}
