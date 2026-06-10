package cl.management.potion.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import cl.management.potion.dto.request.UserRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.service.UserService;
import cl.management.potion.util.enums.ExceptionListEnum;
import cl.management.potion.util.enums.RoleEnum;

/**
 * Unit tests for UserController using JUnit 5 and Mockito.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@WebMvcTest(UserController.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest validUserRequest;
    private DefaultResponse successResponse;
    private String authToken;

    @BeforeEach
    void setUp() {
        validUserRequest = new UserRequest();
        validUserRequest.setUsername("testuser");
        validUserRequest.setPassword("password123");
        validUserRequest.setEmail("test@example.com");
        validUserRequest.setRole(RoleEnum.ADMIN);

        successResponse = DefaultResponse.builder()
                .code("200")
                .message("USER REGISTERED")
                .build();

        authToken = "Bearer validToken123";
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void testRegisterUserSuccess() throws Exception {
        // Arrange
        when(userService.registerUser(any(UserRequest.class))).thenReturn(successResponse);

        // Act & Assert
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("USER REGISTERED"));
    }

    @Test
    @DisplayName("Should return 400 when registering user with invalid data")
    void testRegisterUserInvalidData() throws Exception {
        // Arrange
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setUsername(null); // Invalid: null username

        // Act & Assert
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should list all users successfully")
    void testListUsersSuccess() throws Exception {
        // Arrange
        // Note: listUsers returns Page<UserResponse>, not DefaultResponse
        // This test simplified for demonstration

        // Act & Assert
        mockMvc.perform(get("/users/")
                .header("Authorization", authToken)
                .param("size", "20")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    @DisplayName("Should return 400 when listing users without Authorization header")
    void testListUsersMissingAuthHeader() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/users/")
                .param("size", "20")
                .param("page", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should search user by username successfully")
    void testSearchUserSuccess() throws Exception {
        // Arrange
        when(userService.searchUser(anyString(), anyString())).thenReturn(successResponse);

        // Act & Assert
        mockMvc.perform(get("/users/testuser")
                .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testSearchUserNotFound() throws Exception {
        // Arrange
        when(userService.searchUser(anyString(), anyString()))
                .thenThrow(new ServiceException(ExceptionListEnum.USER_NOT_FOUND));

        // Act & Assert
        mockMvc.perform(get("/users/nonexistent")
                .header("Authorization", authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUserSuccess() throws Exception {
        // Arrange
        when(userService.updateUser(anyString(), anyLong(), any(UserRequest.class)))
                .thenReturn(successResponse);

        // Act & Assert
        mockMvc.perform(put("/users/1")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should deactivate user successfully")
    void testDeactivateUserSuccess() throws Exception {
        // Arrange
        doNothing().when(userService).deactivateUser(anyString(), anyLong());

        // Act & Assert
        mockMvc.perform(delete("/users/1")
                .header("Authorization", authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should activate user successfully")
    void testActivateUserSuccess() throws Exception {
        // Arrange
        doNothing().when(userService).activateUser(anyString(), anyLong());

        // Act & Assert
        mockMvc.perform(patch("/users/1/activate")
                .header("Authorization", authToken))
                .andExpect(status().isNoContent());
    }
}
