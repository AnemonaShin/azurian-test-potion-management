package cl.management.potion.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ExceptionResponse DTO using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("ExceptionResponse Tests")
class ExceptionResponseTest {

    private ExceptionResponse exceptionResponse;

    @BeforeEach
    void setUp() {
        exceptionResponse = new ExceptionResponse();
    }

    @Test
    @DisplayName("Should create ExceptionResponse with no-arg constructor")
    void testExceptionResponseNoArgConstructor() {
        assertNotNull(exceptionResponse);
        assertNull(exceptionResponse.getCode());
        assertNull(exceptionResponse.getMessage());
        assertNull(exceptionResponse.getTimestamp());
    }

    @Test
    @DisplayName("Should create ExceptionResponse with all-arg constructor")
    void testExceptionResponseAllArgConstructor() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        ExceptionResponse response = new ExceptionResponse("500", "INTERNAL SERVER ERROR", now);

        // Act & Assert
        assertEquals("500", response.getCode());
        assertEquals("INTERNAL SERVER ERROR", response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should create ExceptionResponse with builder pattern")
    void testExceptionResponseBuilder() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act
        ExceptionResponse response = ExceptionResponse.builder()
                .code("404")
                .message("NOT FOUND")
                .timestamp(now)
                .build();

        // Assert
        assertEquals("404", response.getCode());
        assertEquals("NOT FOUND", response.getMessage());
        assertEquals(now, response.getTimestamp());
    }

    @Test
    @DisplayName("Should get code from ExceptionResponse")
    void testGetCode() {
        // Arrange
        exceptionResponse = ExceptionResponse.builder()
                .code("401")
                .message("UNAUTHORIZED")
                .build();

        // Act & Assert
        assertEquals("401", exceptionResponse.getCode());
    }

    @Test
    @DisplayName("Should get message from ExceptionResponse")
    void testGetMessage() {
        // Arrange
        exceptionResponse = ExceptionResponse.builder()
                .code("400")
                .message("BAD REQUEST")
                .build();

        // Act & Assert
        assertEquals("BAD REQUEST", exceptionResponse.getMessage());
    }

    @Test
    @DisplayName("Should get timestamp from ExceptionResponse")
    void testGetTimestamp() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        exceptionResponse = ExceptionResponse.builder()
                .code("500")
                .message("ERROR")
                .timestamp(now)
                .build();

        // Act & Assert
        assertNotNull(exceptionResponse.getTimestamp());
        assertEquals(now, exceptionResponse.getTimestamp());
    }

    @Test
    @DisplayName("Should handle null timestamp")
    void testExceptionResponseWithNullTimestamp() {
        // Arrange
        exceptionResponse = ExceptionResponse.builder()
                .code("200")
                .message("OK")
                .timestamp(null)
                .build();

        // Act & Assert
        assertNull(exceptionResponse.getTimestamp());
    }
}
