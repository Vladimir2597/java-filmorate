package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {

        return userStorage.getAllUsers();
    }

    public User getUserById(long userId) {

        return userStorage.findUserById(userId);
    }

    public User createUser(User user) {
        checkNameForNull(user);

        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        checkNameForNull(user);

        return userStorage.updateUser(user);
    }

    public User addFriend(long userId, long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);

        userStorage.updateUser(friend);
        return userStorage.updateUser(user);
    }

    public User deleteFriend(long userId, long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

        user.deleteFriend(friendId);
        friend.deleteFriend(userId);

        userStorage.updateUser(friend);
        return userStorage.updateUser(user);
    }

    public List<User> getUserFriends(long userId) {

        return userStorage.findUserById(userId)
                .getFriends().stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {

        return userStorage.findUserById(userId)
                .getFriends().stream()
                .filter(userStorage.findUserById(otherUserId)
                        .getFriends()::contains)
                .map(userStorage::findUserById)
                .collect(Collectors.toList());

    }

    private void checkNameForNull(User user) {
        if (user.getName() == null ||
                user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
