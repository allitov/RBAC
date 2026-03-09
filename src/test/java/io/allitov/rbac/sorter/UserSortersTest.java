package io.allitov.rbac.sorter;

import static org.assertj.core.api.Assertions.assertThat;

import io.allitov.rbac.model.user.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserSortersTest {

    @Test
    @DisplayName("Должен сортировать пользователей по username")
    void shouldSortByUsername() {
        User u1 = User.of("alice", "Alice", "alice@example.com");
        User u2 = User.of("bob", "Bob", "bob@example.com");
        User u3 = User.of("charlie", "Charlie", "charlie@example.com");
        List<User> users = new ArrayList<>(List.of(u3, u1, u2));

        users.sort(UserSorters.byUsername());

        assertThat(users).containsExactly(u1, u2, u3);
    }

    @Test
    @DisplayName("Должен сортировать пользователей по full name")
    void shouldSortByFullName() {
        User u1 = User.of("alice", "Alice", "a@e.com");
        User u2 = User.of("bob", "Bob", "b@e.com");
        User u3 = User.of("charlie", "Charlie", "c@e.com");
        List<User> users = new ArrayList<>(List.of(u3, u2, u1));

        users.sort(UserSorters.byFullName());

        assertThat(users).containsExactly(u1, u2, u3);
    }

    @Test
    @DisplayName("Должен сортировать пользователей по email")
    void shouldSortByEmail() {
        User u1 = User.of("alice", "A", "alice@example.com");
        User u2 = User.of("bob", "B", "bob@example.com");
        User u3 = User.of("charlie", "C", "charlie@example.com");
        List<User> users = new ArrayList<>(List.of(u3, u2, u1));

        users.sort(UserSorters.byEmail());

        assertThat(users).containsExactly(u1, u2, u3);
    }
}
