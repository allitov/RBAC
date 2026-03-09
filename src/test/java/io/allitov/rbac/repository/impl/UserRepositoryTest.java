package io.allitov.rbac.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import io.allitov.rbac.filter.user.UserFilter;
import io.allitov.rbac.model.user.User;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
        repository.clear();
    }

    @Nested
    @DisplayName("Базовые CRUD операции")
    class CrudTests {

        @Test
        @DisplayName("Добавление пользователя и поиск по ID")
        void addAndFindById() {
            User user = User.of("jdoe", "John Doe", "john@example.com");

            repository.add(user);
            Optional<User> found = repository.findById("jdoe");

            assertThat(found).isPresent().contains(user);
            assertThat(repository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("Ошибка при добавлении дубликата username")
        void addDuplicateThrowsException() {
            User user1 = User.of("jdoe", "John Doe", "john@example.com");
            User user2 = User.of("jdoe", "Other Name", "other@example.com");

            repository.add(user1);

            assertThatThrownBy(() -> repository.add(user2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Duplicate username jdoe");
        }

        @Test
        @DisplayName("Удаление существующего и несуществующего пользователя")
        void removeUser() {
            repository.add(User.of("tester", "Tester", "test@test.com"));

            assertThat(repository.remove("tester")).isTrue();
            assertThat(repository.remove("unknown")).isFalse();
            assertThat(repository.count()).isZero();
        }

        @Test
        @DisplayName("Обновление данных существующего пользователя")
        void updateExistingUser() {
            repository.add(User.of("dev", "Developer", "old@mail.com"));
            User updated = User.of("dev", "Senior Developer", "new@mail.com");

            repository.update(updated);

            User result = repository.findById("dev").orElseThrow();
            assertThat(result.getFullName()).isEqualTo("Senior Developer");
            assertThat(result.getEmail()).isEqualTo("new@mail.com");
        }
    }

    @Nested
    @DisplayName("Поиск и фильтрация")
    class FilterTests {

        @Test
        @DisplayName("Поиск по фильтру")
        void findByFilter() {
            User alice = User.of("alice", "Alice", "alice@test.com");
            User bob = User.of("bob", "Bob", "bob@test.com");
            repository.add(alice);
            repository.add(bob);

            UserFilter filter = Mockito.mock(UserFilter.class);
            when(filter.test(alice)).thenReturn(true);
            when(filter.test(bob)).thenReturn(false);

            List<User> result = repository.findByFilter(filter);

            assertThat(result).hasSize(1).containsExactly(alice);
        }

        @Test
        @DisplayName("Поиск с фильтрацией и сортировкой")
        void findAllWithFilterAndSorter() {
            User u1 = User.of("c_user", "Charlie", "c@test.com");
            User u2 = User.of("a_user", "Anna", "a@test.com");
            User u3 = User.of("b_user", "Boris", "b@test.com");

            repository.add(u1);
            repository.add(u2);
            repository.add(u3);

            Comparator<User> byUsername = Comparator.comparing(User::getUsername);

            List<User> result = repository.findAll(_ -> true, byUsername);

            assertThat(result).containsExactly(u2, u3, u1);
        }
    }
}
