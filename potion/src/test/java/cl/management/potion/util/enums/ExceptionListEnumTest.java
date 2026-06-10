package cl.management.potion.util.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for ExceptionListEnum using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("ExceptionListEnum Tests")
class ExceptionListEnumTest {

    @Test
    @DisplayName("Should have USER_REQUEST_USERNAME_ERROR with BAD_REQUEST status")
    void testUserRequestUsernameError() {
        // Act & Assert
        assertEquals(HttpStatus.BAD_REQUEST, ExceptionListEnum.USER_REQUEST_USERNAME_ERROR.getStatusCode());
        assertEquals("USERNAME IS NULL OR EMPTY", ExceptionListEnum.USER_REQUEST_USERNAME_ERROR.getMessage());
    }

    @Test
    @DisplayName("Should have USER_NOT_FOUND with NOT_FOUND status")
    void testUserNotFound() {
        // Act & Assert
        assertEquals(HttpStatus.NOT_FOUND, ExceptionListEnum.USER_NOT_FOUND.getStatusCode());
        assertEquals("USER NOT FOUND", ExceptionListEnum.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Should have USER_USERNAME_ALREADY_EXIST with CONFLICT status")
    void testUserUsernameAlreadyExist() {
        // Act & Assert
        assertEquals(HttpStatus.CONFLICT, ExceptionListEnum.USER_USERNAME_ALREADY_EXIST.getStatusCode());
        assertEquals("USER 'USERNAME' ALREADY EXISTS", ExceptionListEnum.USER_USERNAME_ALREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("Should have USER_ALREADY_DEACTIVATED with CONFLICT status")
    void testUserAlreadyDeactivated() {
        // Act & Assert
        assertEquals(HttpStatus.CONFLICT, ExceptionListEnum.USER_ALREADY_DEACTIVATED.getStatusCode());
        assertEquals("USER ALREADY DEACTIVATED", ExceptionListEnum.USER_ALREADY_DEACTIVATED.getMessage());
    }

    @Test
    @DisplayName("Should have USER_PASSWORD_INCORRECT with CONFLICT status")
    void testUserPasswordIncorrect() {
        // Act & Assert
        assertEquals(HttpStatus.CONFLICT, ExceptionListEnum.USER_PASSWORD_INCORRECT.getStatusCode());
        assertEquals("INCORRECT PASSWORD", ExceptionListEnum.USER_PASSWORD_INCORRECT.getMessage());
    }

    @Test
    @DisplayName("Should have ROLE_NOT_FOUND with NOT_FOUND status")
    void testRoleNotFound() {
        // Act & Assert
        assertEquals(HttpStatus.NOT_FOUND, ExceptionListEnum.ROLE_NOT_FOUND.getStatusCode());
        assertEquals("ROLE NOT FOUND", ExceptionListEnum.ROLE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Should have ROLE_NAME_ALREADY_EXIST with CONFLICT status")
    void testRoleNameAlreadyExist() {
        // Act & Assert
        assertEquals(HttpStatus.CONFLICT, ExceptionListEnum.ROLE_NAME_ALREADY_EXIST.getStatusCode());
        assertEquals("ROLE 'ROLE NAME' ALREADY EXISTS", ExceptionListEnum.ROLE_NAME_ALREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("Should have DEFAULT_AUTHORIZATION_ERROR with CONFLICT status")
    void testDefaultAuthorizationError() {
        // Act & Assert
        assertEquals(HttpStatus.CONFLICT, ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR.getStatusCode());
        assertEquals("AUTHORIZATION TOKEN IS INVALID, EMPTY, NULL OR DISTINCT",
                ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR.getMessage());
    }

    @Test
    @DisplayName("Should have DEFAULT_ERROR with INTERNAL_SERVER_ERROR status")
    void testDefaultError() {
        // Act & Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionListEnum.DEFAULT_ERROR.getStatusCode());
        assertEquals("INTERNAL SERVER EXCEPTION", ExceptionListEnum.DEFAULT_ERROR.getMessage());
    }

    @Test
    @DisplayName("Should have DEFAULT_REQUEST_NULL_ERROR with BAD_REQUEST status")
    void testDefaultRequestNullError() {
        // Act & Assert
        assertEquals(HttpStatus.BAD_REQUEST, ExceptionListEnum.DEFAULT_REQUEST_NULL_ERROR.getStatusCode());
        assertEquals("REQUEST IS NULL", ExceptionListEnum.DEFAULT_REQUEST_NULL_ERROR.getMessage());
    }

    @Test
    @DisplayName("Should get StatusCode from any ExceptionListEnum")
    void testGetStatusCode() {
        // Act & Assert
        assertNotNull(ExceptionListEnum.USER_NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should get Message from any ExceptionListEnum")
    void testGetMessage() {
        // Act & Assert
        assertNotNull(ExceptionListEnum.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Should have ROLE_DEACTIVATED_UPDATE with NOT_MODIFIED status")
    void testRoleDeactivatedUpdate() {
        // Act & Assert
        assertEquals(HttpStatus.NOT_MODIFIED, ExceptionListEnum.ROLE_DEACTIVATED_UPDATE.getStatusCode());
        assertEquals("ROLE DEACTIVATED CANT BE UPDATED", ExceptionListEnum.ROLE_DEACTIVATED_UPDATE.getMessage());
    }
}
