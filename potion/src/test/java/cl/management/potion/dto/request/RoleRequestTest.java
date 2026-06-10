package cl.management.potion.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for RoleRequest DTO using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("RoleRequest Tests")
class RoleRequestTest {

    private RoleRequest roleRequest;

    @BeforeEach
    void setUp() {
        roleRequest = new RoleRequest();
    }

    @Test
    @DisplayName("Should create RoleRequest with no-arg constructor")
    void testRoleRequestNoArgConstructor() {
        assertNotNull(roleRequest);
    }

    @Test
    @DisplayName("Should create RoleRequest with all-arg constructor")
    void testRoleRequestAllArgConstructor() {
        // Arrange
        RoleRequest request = new RoleRequest("ADMIN", "admin-icon");

        // Act & Assert
        assertEquals("ADMIN", request.getName());
        assertEquals("admin-icon", request.getRoleIcon());
    }

    @Test
    @DisplayName("Should set and get name")
    void testSetAndGetName() {
        // Arrange
        String name = "ADMIN";

        // Act
        roleRequest.setName(name);

        // Assert
        assertEquals(name, roleRequest.getName());
    }

    @Test
    @DisplayName("Should set and get role icon")
    void testSetAndGetRoleIcon() {
        // Arrange
        String roleIcon = "admin-icon";

        // Act
        roleRequest.setRoleIcon(roleIcon);

        // Assert
        assertEquals(roleIcon, roleRequest.getRoleIcon());
    }

    @Test
    @DisplayName("Should handle null values in RoleRequest")
    void testRoleRequestWithNullValues() {
        // Act
        roleRequest.setName(null);
        roleRequest.setRoleIcon(null);

        // Assert - just verify no exceptions are thrown
        assertTrue(true);
    }

    @Test
    @DisplayName("Should handle empty strings in RoleRequest")
    void testRoleRequestWithEmptyStrings() {
        // Act
        roleRequest.setName("");
        roleRequest.setRoleIcon("");

        // Assert
        assertEquals("", roleRequest.getName());
        assertEquals("", roleRequest.getRoleIcon());
    }
}
