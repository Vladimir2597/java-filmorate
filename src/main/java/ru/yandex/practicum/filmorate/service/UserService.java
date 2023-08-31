package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {

        return userStorage.getAll();
    }

    public User getById(long userId) {

        return userStorage.findById(userId);
    }

    public User create(User user) {
        checkNameForNull(user);

        return userStorage.create(user);
    }

    public User update(User user) {
        checkNameForNull(user);

        return userStorage.update(user);
    }

    public User addFriend(long userId, long friendId) {

        return userStorage.addFriend(userId,friendId);
    }

    public User deleteFriend(long userId, long friendId) {

        return userStorage.deleteFriend(userId,friendId);
    }

    public List<User> getFriends(long userId) {

        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {

        return userStorage.getCommonFriends(userId,otherUserId);
    }

    private void checkNameForNull(User user) {
        if (user.getName() == null ||
                user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
