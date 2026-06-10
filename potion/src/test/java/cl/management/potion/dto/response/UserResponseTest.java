package cl.management.potion.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for UserResponse DTO using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("UserResponse Tests")
class UserResponseTest {

    private UserResponse userResponse;
    private RoleResponse mockRole;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse();
        mockRole = RoleResponse.builder()
                .id(1L)
                .name("ADMIN")
                .roleIcon("admin-icon")
                .build();
    }

    @Test
    @DisplayName("Should create UserResponse with no-arg constructor")
    void testUserResponseNoArgConstructor() {
        assertNotNull(userResponse);
        assertNull(userResponse.getId());
        assertNull(userResponse.getUsername());
        assertNull(userResponse.getEmail());
        assertNull(userResponse.getRole());
    }

    @Test
    @DisplayName("Should create UserResponse with all-arg constructor")
    void testUserResponseAllArgConstructor() {
        // Arrange
        UserResponse response = new UserResponse(1L, "testuser", "test@example.com", mockRole, true);

        // Act & Assert
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertNotNull(response.getRole());
    }

    @Test
    @DisplayName("Should create UserResponse with builder pattern")
    void testUserResponseBuilder() {
        // Arrange
        UserResponse response = UserResponse.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(mockRole)
                .build();

        // Act & Assert
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertNotNull(response.getRole());
        assertEquals("ADMIN", response.getRole().getName());
    }

    @Test
    @DisplayName("Should set and get id")
    void testSetAndGetId() {
        // Arrange
        Long id = 1L;

        // Act
        userResponse.setId(id);

        // Assert
        assertEquals(id, userResponse.getId());
    }

    @Test
    @DisplayName("Should set and get username")
    void testSetAndGetUsername() {
        // Arrange
        String username = "testuser";

        // Act
        userResponse.setUsername(username);

        // Assert
        assertEquals(username, userResponse.getUsername());
    }

    @Test
    @DisplayName("Should set and get email")
    void testSetAndGetEmail() {
        // Arrange
        String email = "test@example.com";

        // Act
        userResponse.setEmail(email);

        // Assert
        assertEquals(email, userResponse.getEmail());
    }

    @Test
    @DisplayName("Should set and get role")
    void testSetAndGetRole() {
        // Act
        userResponse.setRole(mockRole);

        // Assert
        assertNotNull(userResponse.getRole());
        assertEquals("ADMIN", userResponse.getRole().getName());
    }

    @Test
    @DisplayName("Should handle null role")
    void testUserResponseWithNullRole() {
        // Act
        userResponse.setId(1L);
        userResponse.setUsername("testuser");
        userResponse.setEmail("test@example.com");
        userResponse.setRole(null);

        // Assert
        assertEquals(1L, userResponse.getId());
        assertEquals("testuser", userResponse.getUsername());
        assertEquals("test@example.com", userResponse.getEmail());
        assertNull(userResponse.getRole());
    }

    @Test
    @DisplayName("Should handle null values")
    void testUserResponseWithNullValues() {
        // Act
        userResponse.setId(null);
        userResponse.setUsername(null);
        userResponse.setEmail(null);
        userResponse.setRole(null);

        // Assert
        assertNull(userResponse.getId());
        assertNull(userResponse.getUsername());
        assertNull(userResponse.getEmail());
        assertNull(userResponse.getRole());
    }
}
