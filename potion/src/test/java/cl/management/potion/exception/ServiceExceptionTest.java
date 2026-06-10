package cl.management.potion.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import cl.management.potion.util.enums.ExceptionListEnum;

/**
 * Unit tests for ServiceException using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("ServiceException Tests")
class ServiceExceptionTest {

    @Test
    @DisplayName("Should create ServiceException with ExceptionListEnum")
    void testServiceExceptionWithEnum() {
        // Arrange
        ExceptionListEnum exceptionEnum = ExceptionListEnum.USER_NOT_FOUND;

        // Act
        ServiceException exception = new ServiceException(exceptionEnum);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404", exception.getCode());
        assertEquals("USER NOT FOUND", exception.getMessage());
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Should create ServiceException with HttpStatus and message")
    void testServiceExceptionWithHttpStatusAndMessage() {
        // Arrange
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Invalid input";

        // Act
        ServiceException exception = new ServiceException(status, message);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("400", exception.getCode());
        assertEquals("Invalid input", exception.getMessage());
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Should create ServiceException with HttpStatus, code, and message")
    void testServiceExceptionWithHttpStatusCodeAndMessage() {
        // Arrange
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = "CUSTOM_ERROR";
        String message = "Custom error message";

        // Act
        ServiceException exception = new ServiceException(status, code, message);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("CUSTOM_ERROR", exception.getCode());
        assertEquals("Custom error message", exception.getMessage());
        assertNotNull(exception.getTimestamp());
    }

    @Test
    @DisplayName("Should get statusCode from ServiceException")
    void testGetStatusCode() {
        // Arrange
        ServiceException exception = new ServiceException(ExceptionListEnum.ROLE_NOT_FOUND);

        // Act & Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should get code from ServiceException")
    void testGetCode() {
        // Arrange
        ServiceException exception = new ServiceException(ExceptionListEnum.USER_PASSWORD_INCORRECT);

        // Act & Assert
        assertEquals("409", exception.getCode());
    }

    @Test
    @DisplayName("Should get message from ServiceException")
    void testGetMessage() {
        // Arrange
        ServiceException exception = new ServiceException(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR);

        // Act & Assert
        assertEquals("AUTHORIZATION TOKEN IS INVALID, EMPTY, NULL OR DISTINCT", exception.getMessage());
    }

    @Test
    @DisplayName("Should get timestamp from ServiceException")
    void testGetTimestamp() {
        // Arrange
        LocalDateTime beforeCreation = LocalDateTime.now();
        ServiceException exception = new ServiceException(ExceptionListEnum.DEFAULT_ERROR);
        LocalDateTime afterCreation = LocalDateTime.now();

        // Act & Assert
        assertNotNull(exception.getTimestamp());
        assert exception.getTimestamp().isAfter(beforeCreation.minusSeconds(1)) ||
                exception.getTimestamp().isEqual(beforeCreation);
        assert exception.getTimestamp().isBefore(afterCreation.plusSeconds(1)) ||
                exception.getTimestamp().isEqual(afterCreation);
    }

    @Test
    @DisplayName("Should handle USER_ALREADY_DEACTIVATED exception")
    void testServiceExceptionUserAlreadyDeactivated() {
        // Arrange
        ExceptionListEnum exceptionEnum = ExceptionListEnum.USER_ALREADY_DEACTIVATED;

        // Act
        ServiceException exception = new ServiceException(exceptionEnum);

        // Assert
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("409", exception.getCode());
        assertEquals("USER ALREADY DEACTIVATED", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle ROLE_NAME_ALREADY_EXIST exception")
    void testServiceExceptionRoleNameAlreadyExist() {
        // Arrange
        ExceptionListEnum exceptionEnum = ExceptionListEnum.ROLE_NAME_ALREADY_EXIST;

        // Act
        ServiceException exception = new ServiceException(exceptionEnum);

        // Assert
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("409", exception.getCode());
        assertEquals("ROLE 'ROLE NAME' ALREADY EXISTS", exception.getMessage());
    }
}
