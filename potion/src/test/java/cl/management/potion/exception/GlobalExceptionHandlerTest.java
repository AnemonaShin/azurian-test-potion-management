package cl.management.potion.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import cl.management.potion.dto.response.ExceptionResponse;
import cl.management.potion.util.enums.ExceptionListEnum;

/**
 * Unit tests for GlobalExceptionHandler using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle ServiceException and return ResponseEntity with ExceptionResponse")
    void testHandleGlobalExceptionUserNotFound() {
        // Arrange
        ServiceException exception = new ServiceException(ExceptionListEnum.USER_NOT_FOUND);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGlobalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("404", response.getBody().getCode());
        assertEquals("USER NOT FOUND", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle ServiceException with ROLE_NOT_FOUND")
    void testHandleGlobalExceptionRoleNotFound() {
        // Arrange
        ServiceException exception = new ServiceException(ExceptionListEnum.ROLE_NOT_FOUND);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGlobalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("404", response.getBody().getCode());
        assertEquals("ROLE NOT FOUND", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle ServiceException with BAD_REQUEST status")
    void testHandleGlobalExceptionBadRequest() {
        // Arrange
        ServiceException exception = new ServiceException(
                ExceptionListEnum.USER_REQUEST_USERNAME_ERROR);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGlobalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("400", response.getBody().getCode());
        assertEquals("USERNAME IS NULL OR EMPTY", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle ServiceException with CONFLICT status")
    void testHandleGlobalExceptionConflict() {
        // Arrange
        ServiceException exception = new ServiceException(ExceptionListEnum.USER_USERNAME_ALREADY_EXIST);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGlobalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("409", response.getBody().getCode());
        assertEquals("USER 'USERNAME' ALREADY EXISTS", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle ServiceException with INTERNAL_SERVER_ERROR status")
    void testHandleGlobalExceptionInternalServerError() {
        // Arrange
        ServiceException exception = new ServiceException(ExceptionListEnum.DEFAULT_ERROR);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGlobalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("500", response.getBody().getCode());
        assertEquals("INTERNAL SERVER EXCEPTION", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle ServiceException with UNAUTHORIZED status")
    void testHandleGlobalExceptionUnauthorized() {
        // Arrange
        ServiceException exception = new ServiceException(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGlobalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("409", response.getBody().getCode());
        assertEquals("AUTHORIZATION TOKEN IS INVALID, EMPTY, NULL OR DISTINCT", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should preserve exception information in response")
    void testHandleGlobalExceptionPreservesInformation() {
        // Arrange
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = "CUSTOM_CODE";
        String message = "Custom error message";
        ServiceException exception = new ServiceException(status, code, message);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGlobalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(status, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(code, response.getBody().getCode());
        assertEquals(message, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle ROLE_DEACTIVATED_UPDATE exception")
    void testHandleGlobalExceptionRoleDeactivatedUpdate() {
        // Arrange
        ServiceException exception = new ServiceException(ExceptionListEnum.ROLE_DEACTIVATED_UPDATE);

        // Act
        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGlobalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("304", response.getBody().getCode());
    }
}
