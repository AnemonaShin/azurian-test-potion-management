package cl.management.potion.util.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for RoleEnum using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("RoleEnum Tests")
class RoleEnumTest {

    @Test
    @DisplayName("Should have ADMIN role with id 1")
    void testAdminRole() {
        // Act & Assert
        assertEquals(1L, RoleEnum.ADMIN.getId());
    }

    @Test
    @DisplayName("Should have CRAFTER role with id 2")
    void testCrafterRole() {
        // Act & Assert
        assertEquals(2L, RoleEnum.CRAFTER.getId());
    }

    @Test
    @DisplayName("Should have GARDENER role with id 3")
    void testGardenerRole() {
        // Act & Assert
        assertEquals(3L, RoleEnum.GARDENER.getId());
    }

    @Test
    @DisplayName("Should get id from ADMIN role")
    void testGetIdFromAdminRole() {
        // Arrange
        RoleEnum role = RoleEnum.ADMIN;

        // Act
        Long id = role.getId();

        // Assert
        assertNotNull(id);
        assertEquals(1L, id);
    }

    @Test
    @DisplayName("Should get id from CRAFTER role")
    void testGetIdFromCrafterRole() {
        // Arrange
        RoleEnum role = RoleEnum.CRAFTER;

        // Act
        Long id = role.getId();

        // Assert
        assertNotNull(id);
        assertEquals(2L, id);
    }

    @Test
    @DisplayName("Should get id from GARDENER role")
    void testGetIdFromGardenerRole() {
        // Arrange
        RoleEnum role = RoleEnum.GARDENER;

        // Act
        Long id = role.getId();

        // Assert
        assertNotNull(id);
        assertEquals(3L, id);
    }

    @Test
    @DisplayName("Should have exactly 3 roles in enum")
    void testRoleEnumCount() {
        // Act
        RoleEnum[] roles = RoleEnum.values();

        // Assert
        assertEquals(3, roles.length);
    }

    @Test
    @DisplayName("Should be able to get role by name")
    void testGetRoleByName() {
        // Act
        RoleEnum adminRole = RoleEnum.valueOf("ADMIN");

        // Assert
        assertNotNull(adminRole);
        assertEquals(RoleEnum.ADMIN, adminRole);
        assertEquals(1L, adminRole.getId());
    }

    @Test
    @DisplayName("Should contain ADMIN in values")
    void testRoleEnumContainsAdmin() {
        // Act & Assert
        assertEquals(RoleEnum.ADMIN, RoleEnum.ADMIN);
    }

    @Test
    @DisplayName("Should contain CRAFTER in values")
    void testRoleEnumContainsCrafter() {
        // Act & Assert
        assertEquals(RoleEnum.CRAFTER, RoleEnum.CRAFTER);
    }

    @Test
    @DisplayName("Should contain GARDENER in values")
    void testRoleEnumContainsGardener() {
        // Act & Assert
        assertEquals(RoleEnum.GARDENER, RoleEnum.GARDENER);
    }
}
