package cl.management.potion.service.implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.model.RoleEntity;
import cl.management.potion.model.UserEntity;
import cl.management.potion.repository.UserRepository;
import cl.management.potion.util.ValidationUtil;
import cl.management.potion.util.enums.ExceptionListEnum;

/**
 * Unit tests for LoginServiceImpl using JUnit 5 and Mockito.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoginServiceImpl Tests")
class LoginServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginServiceImpl loginService;

    private UserEntity mockUser;
    private String validToken;
    private String encodedToken;

    @BeforeEach
    void setUp() {
        // Create mock user
        mockUser = UserEntity.builder()
                .id(1L)
                .username("testuser")
                .password("hashedPassword123")
                .email("test@example.com")
                .role(RoleEntity.builder().id(1L).name("ADMIN").build())
                .build();

        // Create valid token
        String credentials = "testuser:password123";
        validToken = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
        encodedToken = Base64.getEncoder().encodeToString("password123:hashedPassword123".getBytes());
    }

    @Test
    @DisplayName("Should successfully login and return token when credentials are valid")
    void testGetLoginTokenSuccess() throws ServiceException {
        // Arrange
        when(userRepository.searchByUsername("testuser")).thenReturn(mockUser);

        try (MockedStatic<ValidationUtil> mockedValidation = org.mockito.Mockito.mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString(), anyString(), anyString()));

            // Act
            DefaultResponse response = loginService.getLoginToken(validToken);

            // Assert
            assertNotNull(response);
            assertEquals("200", response.getCode());
            assertEquals("TOKEN GENERATED", response.getMessage());
            assertNotNull(response.getResponse());
            verify(userRepository).searchByUsername("testuser");
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when user is not found")
    void testGetLoginTokenUserNotFound() {
        // Arrange
        when(userRepository.searchByUsername(anyString())).thenReturn(null);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loginService.getLoginToken(validToken);
        });

        assertEquals(ExceptionListEnum.USER_NOT_FOUND.getStatusCode(), exception.getStatusCode());
        verify(userRepository).searchByUsername("testuser");
    }

    @Test
    @DisplayName("Should throw ServiceException when password validation fails")
    void testGetLoginTokenInvalidPassword() throws ServiceException {
        // Arrange
        when(userRepository.searchByUsername("testuser")).thenReturn(mockUser);

        try (MockedStatic<ValidationUtil> mockedValidation = org.mockito.Mockito.mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString(), anyString(), anyString()))
                    .thenThrow(new ServiceException(ExceptionListEnum.USER_PASSWORD_INCORRECT));

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                loginService.getLoginToken(validToken);
            });

            assertEquals(ExceptionListEnum.USER_PASSWORD_INCORRECT.getStatusCode(), exception.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when token format is invalid")
    void testGetLoginTokenInvalidTokenFormat() {
        // Arrange
        String invalidToken = "Invalid Format";

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loginService.getLoginToken(invalidToken);
        });

        assertEquals(ExceptionListEnum.DEFAULT_ERROR.getStatusCode(), exception.getStatusCode());
    }

    @Test
    @DisplayName("Should throw ServiceException when token decoding fails")
    void testGetLoginTokenDecodingError() {
        // Arrange
        String malformedToken = "Basic !!!Invalid Base64!!!";

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loginService.getLoginToken(malformedToken);
        });

        assertEquals(ExceptionListEnum.DEFAULT_ERROR.getStatusCode(), exception.getStatusCode());
    }

    @Test
    @DisplayName("Should throw ServiceException when token split is incomplete")
    void testGetLoginTokenIncompleteTokenSplit() {
        // Arrange
        String incompleteToken = "Basic " + Base64.getEncoder().encodeToString("noColonHere".getBytes());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loginService.getLoginToken(incompleteToken);
        });

        assertEquals(ExceptionListEnum.DEFAULT_ERROR.getStatusCode(), exception.getStatusCode());
    }
}
