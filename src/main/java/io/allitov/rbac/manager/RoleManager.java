package io.allitov.rbac.manager;

import io.allitov.rbac.filter.role.RoleFilter;
import io.allitov.rbac.model.role.Permission;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.repository.impl.RoleRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleManager {

    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow();
    }

    public List<Role> findByFilter(RoleFilter filter) {
        return roleRepository.findByFilter(filter);
    }

    public List<Role> findAll(RoleFilter filter, Comparator<Role> sorter) {
        return roleRepository.findAll(filter, sorter);
    }

    public boolean exists(String name) {
        return roleRepository.findByName(name).isPresent();
    }

    public void addPermissionToRole(String roleName, Permission permission) {
        Role foundRole = findByName(roleName);
        foundRole.addPermission(permission);
        roleRepository.update(foundRole);
    }

    public void removePermissionFromRole(String roleName, Permission permission) {
        Role foundRole = findByName(roleName);
        foundRole.removePermission(permission);
        roleRepository.update(foundRole);
    }

    public List<Role> findRolesWithPermission(String permissionName, String resource) {
        return roleRepository.findAllRolesWithPermissionAndResource(permissionName, resource);
    }

    public void update(String newName, String newDescription) {
        roleRepository.update(Role.of(newName, newDescription));
    }

    public void add(Role item) {
        roleRepository.add(item);
    }

    public boolean remove(Role item) {
        return roleRepository.remove(item.getId());
    }

    public Role findById(String id) {
        return roleRepository.findById(id).orElseThrow();
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public int count() {
        return roleRepository.count();
    }
}
