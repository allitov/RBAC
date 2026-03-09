package io.allitov.rbac.manager;

import io.allitov.rbac.filter.user.UserFilter;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.impl.UserRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserManager {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findById(username).orElseThrow();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public List<User> findByFilter(UserFilter filter) {
        return userRepository.findByFilter(filter);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAll(UserFilter filter, Comparator<User> sorter) {
        return userRepository.findAll(filter, sorter);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findById(username).isPresent();
    }

    public void update(String username, String newFullName, String newEmail) {
        userRepository.update(User.of(username, newFullName, newEmail));
    }

    public void add(User item) {
        userRepository.add(item);
    }

    public boolean remove(String username) {
        return userRepository.remove(username);
    }

    public int count() {
        return userRepository.count();
    }
}
