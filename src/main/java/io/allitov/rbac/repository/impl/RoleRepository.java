package io.allitov.rbac.repository.impl;

import io.allitov.rbac.filter.role.RoleFilter;
import io.allitov.rbac.filter.role.RoleFilters;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.repository.Repository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RoleRepository implements Repository<Role, String> {

    private static final Map<String, Role> ROLE_BASE = new HashMap<>();
    private static final Map<String, Role> NAME_INDEX = new HashMap<>();

    @Override
    public void add(Role role) {
        if (ROLE_BASE.putIfAbsent(role.getId(), role) != null) {
            throw new IllegalStateException("Duplicate role: " + role.getId());
        }
        NAME_INDEX.put(role.getName(), role);
    }

    @Override
    public boolean remove(String id) {
        Role deletedRole = ROLE_BASE.remove(id);
        if (deletedRole == null) {
            return false;
        }
        NAME_INDEX.remove(deletedRole.getName());
        return true;
    }

    @Override
    public Optional<Role> findById(String id) {
        return Optional.ofNullable(ROLE_BASE.get(id));
    }

    @Override
    public List<Role> findAll() {
        return ROLE_BASE.values().stream().toList();
    }

    @Override
    public int count() {
        return ROLE_BASE.size();
    }

    @Override
    public void clear() {
        ROLE_BASE.clear();
        NAME_INDEX.clear();
    }

    public Optional<Role> findByName(String name) {
        return Optional.ofNullable(NAME_INDEX.get(name));
    }

    public List<Role> findByFilter(RoleFilter filter) {
        return ROLE_BASE.values().stream().filter(filter::test).toList();
    }

    public List<Role> findAll(RoleFilter filter, Comparator<Role> sorter) {
        return findByFilter(filter).stream().sorted(sorter).toList();
    }

    public void update(Role role) {
        if (ROLE_BASE.computeIfPresent(role.getId(), (_, _) -> role) == null) {
            throw new IllegalStateException("Role with id '%s' doesn't exist".formatted(role.getId()));
        }
    }

    public List<Role> findAllRolesWithPermissionAndResource(String permissionName, String resource) {
        return findByFilter(RoleFilters.hasPermission(permissionName, resource));
    }
}
