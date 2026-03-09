package io.allitov.rbac.repository.impl;

import io.allitov.rbac.filter.user.UserFilter;
import io.allitov.rbac.filter.user.UserFilters;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.Repository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserRepository implements Repository<User, String> {

    private static final Map<String, User> USER_BASE = new HashMap<>();

    @Override
    public void add(User user) {
        if (USER_BASE.putIfAbsent(user.getUsername(), user) != null) {
            throw new IllegalStateException("Duplicate username " + user.getUsername());
        }
    }

    @Override
    public boolean remove(String username) {
        return USER_BASE.remove(username) != null;
    }

    @Override
    public Optional<User> findById(String username) {
        return Optional.ofNullable(USER_BASE.get(username));
    }

    @Override
    public List<User> findAll() {
        return USER_BASE.values().stream().toList();
    }

    @Override
    public int count() {
        return USER_BASE.size();
    }

    @Override
    public void clear() {
        USER_BASE.clear();
    }

    public void update(User user) {
        if (USER_BASE.computeIfPresent(user.getUsername(), (_, _) -> user) == null) {
            throw new IllegalStateException("User with username '%s' doesn't exist".formatted(user.getUsername()));
        }
    }

    public List<User> findByFilter(UserFilter filter) {
        return USER_BASE.values().stream().filter(filter::test).toList();
    }

    public Optional<User> findByEmail(String email) {
        return findByFilter(UserFilters.byEmail(email)).stream().findFirst();
    }

    public List<User> findAll(UserFilter filter, Comparator<User> sorter) {
        return findByFilter(filter).stream().sorted(sorter).toList();
    }
}
