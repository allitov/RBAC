package io.allitov.rbac.repository.impl;

import io.allitov.rbac.filter.assignment.AssignmentFilter;
import io.allitov.rbac.filter.assignment.AssignmentFilters;
import io.allitov.rbac.model.assignment.RoleAssignment;
import io.allitov.rbac.model.role.Role;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.Repository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AssignmentRepository implements Repository<RoleAssignment, String> {

    private static final Map<String, RoleAssignment> ASSIGNMENT_BASE = new HashMap<>();

    @Override
    public void add(RoleAssignment assignment) {
        if (ASSIGNMENT_BASE.putIfAbsent(assignment.assignmentId(), assignment) != null) {
            throw new IllegalStateException("Duplicate assignment " + assignment.assignmentId());
        }
    }

    @Override
    public boolean remove(String id) {
        return ASSIGNMENT_BASE.remove(id) != null;
    }

    @Override
    public Optional<RoleAssignment> findById(String id) {
        return Optional.ofNullable(ASSIGNMENT_BASE.get(id));
    }

    @Override
    public List<RoleAssignment> findAll() {
        return ASSIGNMENT_BASE.values().stream().toList();
    }

    @Override
    public int count() {
        return ASSIGNMENT_BASE.size();
    }

    @Override
    public void clear() {
        ASSIGNMENT_BASE.clear();
    }

    public void update(RoleAssignment assignment) {
        if (ASSIGNMENT_BASE.computeIfPresent(assignment.assignmentId(), (_, _) -> assignment) == null) {
            throw new IllegalStateException(
                    "Assignment with id '%s' doesn't exist".formatted(assignment.assignmentId()));
        }
    }

    public List<RoleAssignment> findByFilter(AssignmentFilter filter) {
        return ASSIGNMENT_BASE.values().stream().filter(filter::test).toList();
    }

    public List<RoleAssignment> findByUser(User user) {
        return findByFilter(AssignmentFilters.byUser(user));
    }

    public List<RoleAssignment> findByRole(Role role) {
        return findByFilter(AssignmentFilters.byRole(role));
    }

    public List<RoleAssignment> findAll(AssignmentFilter filter, Comparator<RoleAssignment> sorter) {
        return findByFilter(filter).stream().sorted(sorter).toList();
    }

    public List<RoleAssignment> findAllActiveAssignments() {
        return findByFilter(AssignmentFilters.activeOnly());
    }
}
