package io.allitov.rbac.repository.role;

import io.allitov.rbac.filter.role.RoleFilter;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.repository.Repository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RoleManager implements Repository<Role> {

    private static final Map<String, Role> ROLE_BASE = new HashMap<>();
    private static final Map<String, Role> NAME_INDEX = new HashMap<>();

    public Optional<Role> findByName(String name) {
        return Optional.ofNullable(NAME_INDEX.get(name));
    }

    public List<Role> findByFilter(RoleFilter filter) {
        return ROLE_BASE.values().stream().filter(filter::test).toList();
    }

    public List<Role> findAll(RoleFilter filter, Comparator<Role> sorter) {
        return findByFilter(filter).stream().sorted(sorter).toList();
    }

    public boolean exists(String name) {
        return NAME_INDEX.containsKey(name);
    }

    public void addPermissionToRole(String roleName, Permission permission) {
        NAME_INDEX.computeIfPresent(roleName, (_, role) -> {
            role.addPermission(permission);
            return role;
        });
    }

    public void removePermissionFromRole(String roleName, Permission permission) {
        NAME_INDEX.computeIfPresent(roleName, (_, role) -> {
            role.removePermission(permission);
            return role;
        });
    }

    public List<Role> findRolesWithPermission(String permissionName, String resource) {
        return ROLE_BASE.values().stream()
                .filter(role -> role.hasPermission(permissionName, resource))
                .toList();
    }

    @Override
    public void add(Role item) {
        if (ROLE_BASE.putIfAbsent(item.getId(), item) != null) {
            throw new IllegalStateException("Duplicate role: " + item.getId());
        }
        NAME_INDEX.put(item.getName(), item);
    }

    @Override
    public boolean remove(Role item) {
        Role deletedRole = ROLE_BASE.remove(item.getId());
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
        return new ArrayList<>(ROLE_BASE.values());
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
}
