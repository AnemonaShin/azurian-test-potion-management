package cl.management.potion.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.service.LoginService;
import cl.management.potion.util.enums.ExceptionListEnum;

/**
 * Unit tests for LoginController using JUnit 5 and Mockito.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("LoginController Tests")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    private DefaultResponse successResponse;
    private String validAuthToken;

    @BeforeEach
    void setUp() {
        var response = new HashMap<String, Object>();
        response.put("token", Base64.getEncoder().encodeToString("secret:password123".getBytes()));

        successResponse = DefaultResponse.builder()
                .code("200")
                .message("TOKEN GENERATED")
                .response(response)
                .build();

        validAuthToken = "Basic " + Base64.getEncoder().encodeToString("user:password123".getBytes());
    }

    @Test
    @DisplayName("Should return 200 with token when valid credentials are provided")
    void testGetLoginTokenSuccess() throws Exception {
        // Arrange
        when(loginService.getLoginToken(anyString())).thenReturn(successResponse);

        // Act & Assert
        mockMvc.perform(get("/login/")
                .header("Authorization", validAuthToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("TOKEN GENERATED"))
                .andExpect(jsonPath("$.response.token").exists());
    }

    @Test
    @DisplayName("Should throw ServiceException when user not found")
    void testGetLoginTokenUserNotFound() throws Exception {
        // Arrange
        when(loginService.getLoginToken(anyString()))
                .thenThrow(new ServiceException(ExceptionListEnum.USER_NOT_FOUND));

        // Act & Assert
        mockMvc.perform(get("/login/")
                .header("Authorization", validAuthToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when Authorization header is missing")
    void testGetLoginTokenMissingAuthorizationHeader() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should throw ServiceException with default authorization error")
    void testGetLoginTokenInvalidTokenFormat() throws Exception {
        // Arrange
        when(loginService.getLoginToken(anyString()))
                .thenThrow(new ServiceException(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR));

        // Act & Assert
        mockMvc.perform(get("/login/")
                .header("Authorization", "InvalidFormat"))
                .andExpect(status().isConflict());
    }
}
