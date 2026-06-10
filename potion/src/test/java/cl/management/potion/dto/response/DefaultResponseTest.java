package cl.management.potion.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for DefaultResponse DTO using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("DefaultResponse Tests")
class DefaultResponseTest {

    private DefaultResponse response;

    @BeforeEach
    void setUp() {
        response = new DefaultResponse();
    }

    @Test
    @DisplayName("Should create DefaultResponse with no-arg constructor")
    void testDefaultResponseNoArgConstructor() {
        assertNotNull(response);
        assertNull(response.getCode());
        assertNull(response.getMessage());
        assertNull(response.getResponse());
    }

    @Test
    @DisplayName("Should create DefaultResponse with all-arg constructor")
    void testDefaultResponseAllArgConstructor() {
        // Arrange
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", "abc123");

        DefaultResponse resp = new DefaultResponse("200", "SUCCESS", responseBody);

        // Act & Assert
        assertEquals("200", resp.getCode());
        assertEquals("SUCCESS", resp.getMessage());
        assertNotNull(resp.getResponse());
    }

    @Test
    @DisplayName("Should set and get code")
    void testSetAndGetCode() {
        // Arrange
        String code = "200";

        // Act
        response.setCode(code);

        // Assert
        assertEquals(code, response.getCode());
    }

    @Test
    @DisplayName("Should set and get message")
    void testSetAndGetMessage() {
        // Arrange
        String message = "SUCCESS";

        // Act
        response.setMessage(message);

        // Assert
        assertEquals(message, response.getMessage());
    }

    @Test
    @DisplayName("Should set and get response object")
    void testSetAndGetResponse() {
        // Arrange
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", "abc123");

        // Act
        response.setResponse(responseBody);

        // Assert
        assertNotNull(response.getResponse());
        assertEquals(responseBody, response.getResponse());
    }

    @Test
    @DisplayName("Should create DefaultResponse with builder pattern")
    void testDefaultResponseBuilder() {
        // Arrange
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", 1);

        // Act
        DefaultResponse builtResponse = DefaultResponse.builder()
                .code("201")
                .message("CREATED")
                .response(responseBody)
                .build();

        // Assert
        assertEquals("201", builtResponse.getCode());
        assertEquals("CREATED", builtResponse.getMessage());
        assertNotNull(builtResponse.getResponse());
    }

    @Test
    @DisplayName("Should handle null response field")
    void testDefaultResponseWithNullResponse() {
        // Arrange
        response.setCode("200");
        response.setMessage("OK");
        response.setResponse(null);

        // Act & Assert
        assertEquals("200", response.getCode());
        assertEquals("OK", response.getMessage());
        assertNull(response.getResponse());
    }
}
