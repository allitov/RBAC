package io.allitov.rbac.filter.user;

import static org.assertj.core.api.Assertions.assertThat;

import io.allitov.rbac.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserFiltersTest {

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.of("alice_dev", "Alice Smith", "alice@company.com");
        user2 = User.of("bob_manager", "Bob Jones", "bob@external.org");
    }

    @Test
    @DisplayName("Фильтр byUsername должен корректно проверять точное совпадение")
    void shouldFilterByUsername() {
        UserFilter filter = UserFilters.byUsername("alice_dev");

        assertThat(filter.test(user1)).isTrue();
        assertThat(filter.test(user2)).isFalse();
    }

    @Test
    @DisplayName("Фильтр byEmailDomain должен проверять домен почты")
    void shouldFilterByEmailDomain() {
        UserFilter filter = UserFilters.byEmailDomain("company.com");

        assertThat(filter.test(user1)).isTrue();
        assertThat(filter.test(user2)).isFalse();
    }

    @Test
    @DisplayName("Проверка композиции фильтров (AND)")
    void shouldComposeFiltersWithAnd() {
        UserFilter isAlice = UserFilters.byUsername("alice_dev");
        UserFilter isCompanyEmail = UserFilters.byEmailDomain("company.com");

        UserFilter combined = isAlice.and(isCompanyEmail);

        assertThat(combined.test(user1)).isTrue();
        assertThat(combined.test(user2)).isFalse();
    }
}
