package io.allitov.rbac.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.allitov.rbac.model.user.User;
import io.allitov.rbac.repository.impl.UserRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserManagerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserManager userManager;

    @Nested
    @DisplayName("Методы поиска")
    class FindingTests {

        @Test
        @DisplayName("findByUsername: должен возвращать пользователя, если он найден")
        void findByUsernameShouldReturnUser() {
            User user = User.of("johndoe", "John Doe", "john@test.com");
            when(userRepository.findById("johndoe")).thenReturn(Optional.of(user));

            User result = userManager.findByUsername("johndoe");

            assertThat(result).isEqualTo(user);
            verify(userRepository).findById("johndoe");
        }

        @Test
        @DisplayName("findByUsername: должен выбрасывать NoSuchElementException, если не найден")
        void findByUsernameShouldThrowException() {
            when(userRepository.findById("unknown")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userManager.findByUsername("unknown")).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("findByEmail: должен работать аналогично поиску по username")
        void findByEmailShouldReturnUser() {
            User user = User.of("johndoe", "John Doe", "john@test.com");
            when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));

            User result = userManager.findByEmail("john@test.com");

            assertThat(result).isEqualTo(user);
        }
    }

    @Nested
    @DisplayName("Операции изменения данных")
    class ModificationTests {

        @Test
        @DisplayName("update: должен создавать новый объект User и передавать его в репозиторий")
        void updateShouldCallRepositoryWithCorrectUser() {
            userManager.update("johndoe", "New Name", "new@test.com");

            verify(userRepository)
                    .update(argThat(user -> user.getUsername().equals("johndoe")
                            && user.getFullName().equals("New Name")
                            && user.getEmail().equals("new@test.com")));
        }

        @Test
        @DisplayName("add: должен просто делегировать вызов")
        void addShouldDelegateToRepository() {
            User user = User.of("alice", "Alice", "alice@test.com");

            userManager.add(user);

            verify(userRepository).add(user);
        }

        @Test
        @DisplayName("remove: должен возвращать результат выполнения репозитория")
        void removeShouldReturnBoolean() {
            when(userRepository.remove("bob")).thenReturn(true);

            boolean result = userManager.remove("bob");

            assertThat(result).isTrue();
            verify(userRepository).remove("bob");
        }
    }

    @Test
    @DisplayName("existsByUsername: корректная проверка существования")
    void existsByUsernameTest() {
        when(userRepository.findById("exists")).thenReturn(Optional.of(mock(User.class)));
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        assertThat(userManager.existsByUsername("exists")).isTrue();
        assertThat(userManager.existsByUsername("missing")).isFalse();
    }
}
