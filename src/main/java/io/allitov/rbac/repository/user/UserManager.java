package io.allitov.rbac.repository.user;

import io.allitov.rbac.filter.user.UserFilter;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.Repository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.Strings;

public class UserManager implements Repository<User> {

    private static final Map<String, User> USER_BASE = new HashMap<>();

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(USER_BASE.get(username));
    }

    public Optional<User> findByEmail(String email) {
        return USER_BASE.values().stream()
                .filter(user -> Strings.CS.equals(user.email(), email))
                .findFirst();
    }

    public List<User> findByFilter(UserFilter filter) {
        return USER_BASE.values().stream().filter(filter::test).toList();
    }

    public List<User> findAll(UserFilter filter, Comparator<User> sorter) {
        return findByFilter(filter).stream().sorted(sorter).toList();
    }

    public boolean exists(String username) {
        return USER_BASE.containsKey(username);
    }

    public void update(String username, String newFullName, String newEmail) {
        findByUsername(username)
                .ifPresentOrElse(_ -> USER_BASE.put(username, new User(username, newFullName, newEmail)), () -> {
                    throw new IllegalArgumentException("Username '%s' does not exist".formatted(username));
                });
    }

    @Override
    public void add(User item) {
        if (USER_BASE.putIfAbsent(item.username(), item) != null) {
            throw new IllegalStateException("Duplicate username: " + item.username());
        }
    }

    @Override
    public boolean remove(User item) {
        return USER_BASE.remove(item.username()) != null;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(USER_BASE.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(USER_BASE.values());
    }

    @Override
    public int count() {
        return USER_BASE.size();
    }

    @Override
    public void clear() {
        USER_BASE.clear();
    }
}
