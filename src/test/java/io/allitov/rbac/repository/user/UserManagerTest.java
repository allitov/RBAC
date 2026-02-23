package io.allitov.rbac.repository.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.allitov.rbac.filter.user.UserFilters;
import io.allitov.rbac.model.user.User;
import io.allitov.rbac.sorter.user.UserSorters;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserManagerTest {

    private final User admin = new User("admin", "System Admin", "admin@corp.com");
    private final User alice = new User("alice", "Alice Smith", "alice@gmail.com");
    private final User bob = new User("bob", "Bob Brown", "bob@gmail.com");

    private UserManager userManager;

    @BeforeEach
    void setUp() {
        userManager = new UserManager();
        userManager.clear();
    }

    @Test
    @DisplayName("Should add and find user by username")
    void shouldAddAndFindUser() {
        userManager.add(admin);

        Optional<User> found = userManager.findByUsername("admin");

        assertThat(found).isPresent().contains(admin);
        assertThat(userManager.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should add and find user by email")
    void shouldFindUserByEmail() {
        userManager.add(admin);

        Optional<User> found = userManager.findByEmail("admin@corp.com");

        assertThat(found).isPresent().contains(admin);
        assertThat(userManager.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate username")
    void shouldThrowOnDuplicate() {
        userManager.add(admin);

        assertThatThrownBy(() -> userManager.add(admin))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate username: admin");
    }

    @Test
    @DisplayName("Should update existing user")
    void shouldUpdateUser() {
        userManager.add(alice);
        String newName = "Alice Wonderland";
        String newEmail = "wonder@land.com";

        userManager.update("alice", newName, newEmail);

        User updated = userManager.findByUsername("alice").orElseThrow();
        assertThat(updated.fullName()).isEqualTo(newName);
        assertThat(updated.email()).isEqualTo(newEmail);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void shouldThrowOnUpdateMissing() {
        assertThatThrownBy(() -> userManager.update("ghost", "Name", "email@com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username 'ghost' does not exist");
    }

    @Test
    @DisplayName("Should find users by filter")
    void shouldFilterByEmailDomain() {
        userManager.add(admin);
        userManager.add(alice);
        userManager.add(bob);

        List<User> gmailUsers = userManager.findByFilter(UserFilters.byEmailDomain("gmail.com"));

        assertThat(gmailUsers).hasSize(2).containsExactlyInAnyOrder(alice, bob);
    }

    @Test
    @DisplayName("Should find and sort users (Filter + Sorter)")
    void shouldFindAllWithFilterAndSorter() {
        userManager.add(alice);
        userManager.add(bob);
        userManager.add(admin);

        List<User> sortedUsers = userManager.findAll(
                UserFilters.byUsernameContains("a").or(UserFilters.byUsernameContains("b")), UserSorters.byFullName());

        assertThat(sortedUsers).containsExactly(alice, bob, admin);
    }

    @Test
    @DisplayName("Should remove user")
    void shouldRemoveUser() {
        userManager.add(admin);
        boolean removed = userManager.remove(admin);

        assertThat(removed).isTrue();
        assertThat(userManager.exists("admin")).isFalse();
    }

    @Test
    @DisplayName("Should return true when user is deleted and false when user does not exist")
    void shouldReturnCorrectStatusOnRemove() {
        userManager.add(alice);

        boolean removed = userManager.remove(alice);
        assertThat(removed).isTrue();
        assertThat(userManager.exists(alice.username())).isFalse();

        boolean removedAgain = userManager.remove(alice);
        assertThat(removedAgain).isFalse();
    }

    @Test
    @DisplayName("Should return user for valid id and empty optional for invalid id")
    void shouldFindByIdOrReturnEmpty() {
        userManager.add(bob);

        assertThat(userManager.findById("bob")).isPresent().contains(bob);

        assertThat(userManager.findById("non_existent_id")).isEmpty();

        assertThat(userManager.findById(null)).isEmpty();
    }

    @Test
    @DisplayName("Should return a copy of all users")
    void shouldReturnAllUsers() {
        userManager.add(alice);
        userManager.add(bob);

        List<User> allUsers = userManager.findAll();

        assertThat(allUsers).hasSize(2).containsExactlyInAnyOrder(alice, bob);

        allUsers.clear();
        assertThat(userManager.count()).isEqualTo(2);
    }
}
