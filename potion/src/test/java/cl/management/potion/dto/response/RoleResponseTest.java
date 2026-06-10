package cl.management.potion.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for RoleResponse DTO using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("RoleResponse Tests")
class RoleResponseTest {

    private RoleResponse roleResponse;

    @BeforeEach
    void setUp() {
        roleResponse = new RoleResponse();
    }

    @Test
    @DisplayName("Should create RoleResponse with no-arg constructor")
    void testRoleResponseNoArgConstructor() {
        assertNotNull(roleResponse);
        assertNull(roleResponse.getId());
        assertNull(roleResponse.getName());
        assertNull(roleResponse.getRoleIcon());
    }

    @Test
    @DisplayName("Should create RoleResponse with all-arg constructor")
    void testRoleResponseAllArgConstructor() {
        // Arrange
        RoleResponse response = new RoleResponse(1L, "ADMIN", "admin-icon");

        // Act & Assert
        assertEquals(1L, response.getId());
        assertEquals("ADMIN", response.getName());
        assertEquals("admin-icon", response.getRoleIcon());
    }

    @Test
    @DisplayName("Should create RoleResponse with builder pattern")
    void testRoleResponseBuilder() {
        // Arrange
        RoleResponse response = RoleResponse.builder()
                .id(2L)
                .name("USER")
                .roleIcon("user-icon")
                .build();

        // Act & Assert
        assertEquals(2L, response.getId());
        assertEquals("USER", response.getName());
        assertEquals("user-icon", response.getRoleIcon());
    }

    @Test
    @DisplayName("Should set and get id")
    void testSetAndGetId() {
        // Arrange
        Long id = 1L;

        // Act
        roleResponse.setId(id);

        // Assert
        assertEquals(id, roleResponse.getId());
    }

    @Test
    @DisplayName("Should set and get name")
    void testSetAndGetName() {
        // Arrange
        String name = "ADMIN";

        // Act
        roleResponse.setName(name);

        // Assert
        assertEquals(name, roleResponse.getName());
    }

    @Test
    @DisplayName("Should set and get roleIcon")
    void testSetAndGetRoleIcon() {
        // Arrange
        String roleIcon = "admin-icon";

        // Act
        roleResponse.setRoleIcon(roleIcon);

        // Assert
        assertEquals(roleIcon, roleResponse.getRoleIcon());
    }

    @Test
    @DisplayName("Should handle null values in RoleResponse")
    void testRoleResponseWithNullValues() {
        // Act
        roleResponse.setId(null);
        roleResponse.setName(null);
        roleResponse.setRoleIcon(null);

        // Assert
        assertNull(roleResponse.getId());
        assertNull(roleResponse.getName());
        assertNull(roleResponse.getRoleIcon());
    }
}
