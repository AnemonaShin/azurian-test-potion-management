package cl.management.potion.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cl.management.potion.util.enums.RoleEnum;

/**
 * Unit tests for UserRequest DTO using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("UserRequest Tests")
class UserRequestTest {

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
    }

    @Test
    @DisplayName("Should create UserRequest with no-arg constructor")
    void testUserRequestNoArgConstructor() {
        assertNotNull(userRequest);
    }

    @Test
    @DisplayName("Should create UserRequest with all-arg constructor")
    void testUserRequestAllArgConstructor() {
        // Arrange
        UserRequest request = new UserRequest("testuser", "password123", "test@example.com", RoleEnum.ADMIN);

        // Act & Assert
        assertEquals("testuser", request.getUsername());
        assertEquals("password123", request.getPassword());
        assertEquals("test@example.com", request.getEmail());
        assertEquals(RoleEnum.ADMIN, request.getRole());
    }

    @Test
    @DisplayName("Should set and get username")
    void testSetAndGetUsername() {
        // Arrange
        String username = "testuser";

        // Act
        userRequest.setUsername(username);

        // Assert
        assertEquals(username, userRequest.getUsername());
    }

    @Test
    @DisplayName("Should set and get password")
    void testSetAndGetPassword() {
        // Arrange
        String password = "password123";

        // Act
        userRequest.setPassword(password);

        // Assert
        assertEquals(password, userRequest.getPassword());
    }

    @Test
    @DisplayName("Should set and get email")
    void testSetAndGetEmail() {
        // Arrange
        String email = "test@example.com";

        // Act
        userRequest.setEmail(email);

        // Assert
        assertEquals(email, userRequest.getEmail());
    }

    @Test
    @DisplayName("Should set and get role")
    void testSetAndGetRole() {
        // Arrange
        RoleEnum role = RoleEnum.ADMIN;

        // Act
        userRequest.setRole(role);

        // Assert
        assertEquals(role, userRequest.getRole());
    }

    @Test
    @DisplayName("Should handle null values in UserRequest")
    void testUserRequestWithNullValues() {
        // Act
        userRequest.setUsername(null);
        userRequest.setPassword(null);
        userRequest.setEmail(null);
        userRequest.setRole(null);

        // Assert - just verify no exceptions are thrown
        assertTrue(true);
    }
}
