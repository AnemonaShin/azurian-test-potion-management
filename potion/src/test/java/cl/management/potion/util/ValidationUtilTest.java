package cl.management.potion.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cl.management.potion.dto.request.UserRequest;
import cl.management.potion.dto.request.RoleRequest;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.util.enums.ExceptionListEnum;
import cl.management.potion.util.enums.RoleEnum;

/**
 * Unit tests for ValidationUtil using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("ValidationUtil Tests")
class ValidationUtilTest {

    @Test
    @DisplayName("Should not throw exception when validating non-null value")
    void testValidateNotNullSuccess() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validateNotNull("value", ExceptionListEnum.DEFAULT_ERROR);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when value is null")
    void testValidateNotNullThrowsException() {
        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.validateNotNull(null, ExceptionListEnum.DEFAULT_ERROR);
        });
    }

    @Test
    @DisplayName("Should not throw exception when validating non-empty string")
    void testValidateNotEmptySuccess() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.validateNotEmpty("value", ExceptionListEnum.USER_REQUEST_USERNAME_ERROR);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when string is empty")
    void testValidateNotEmptyThrowsExceptionForEmpty() {
        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.validateNotEmpty("", ExceptionListEnum.USER_REQUEST_USERNAME_ERROR);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when string is null")
    void testValidateNotEmptyThrowsExceptionForNull() {
        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.validateNotEmpty(null, ExceptionListEnum.USER_REQUEST_USERNAME_ERROR);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when string contains only whitespace")
    void testValidateNotEmptyThrowsExceptionForWhitespace() {
        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.validateNotEmpty("   ", ExceptionListEnum.USER_REQUEST_USERNAME_ERROR);
        });
    }

    @Test
    @DisplayName("Should not throw exception when validating valid UserRequest")
    void testUserRequestValidationSuccess() {
        // Arrange
        UserRequest validRequest = new UserRequest();
        validRequest.setUsername("testuser");
        validRequest.setPassword("password123");
        validRequest.setEmail("test@example.com");
        validRequest.setRole(RoleEnum.ADMIN);

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.userRequestValidation(validRequest);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when UserRequest is null")
    void testUserRequestValidationThrowsExceptionForNull() {
        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.userRequestValidation(null);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when username is null")
    void testUserRequestValidationThrowsExceptionForNullUsername() {
        // Arrange
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setUsername(null);
        invalidRequest.setPassword("password123");
        invalidRequest.setEmail("test@example.com");
        invalidRequest.setRole(RoleEnum.ADMIN);

        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.userRequestValidation(invalidRequest);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when password is empty")
    void testUserRequestValidationThrowsExceptionForEmptyPassword() {
        // Arrange
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setUsername("testuser");
        invalidRequest.setPassword("");
        invalidRequest.setEmail("test@example.com");
        invalidRequest.setRole(RoleEnum.ADMIN);

        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.userRequestValidation(invalidRequest);
        });
    }

    @Test
    @DisplayName("Should not throw exception when validating valid RoleRequest")
    void testRoleRequestValidationSuccess() {
        // Arrange
        RoleRequest validRequest = new RoleRequest();
        validRequest.setName("ADMIN");
        validRequest.setRoleIcon("admin-icon");

        // Act & Assert
        assertDoesNotThrow(() -> {
            ValidationUtil.roleRequestValidation(validRequest);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when RoleRequest is null")
    void testRoleRequestValidationThrowsExceptionForNull() {
        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.roleRequestValidation(null);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when role name is empty")
    void testRoleRequestValidationThrowsExceptionForEmptyName() {
        // Arrange
        RoleRequest invalidRequest = new RoleRequest();
        invalidRequest.setName("");
        invalidRequest.setRoleIcon("admin-icon");

        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.roleRequestValidation(invalidRequest);
        });
    }

    @Test
    @DisplayName("Should throw ServiceException when roleIcon is null")
    void testRoleRequestValidationThrowsExceptionForNullIcon() {
        // Arrange
        RoleRequest invalidRequest = new RoleRequest();
        invalidRequest.setName("ADMIN");
        invalidRequest.setRoleIcon(null);

        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            ValidationUtil.roleRequestValidation(invalidRequest);
        });
    }
}
