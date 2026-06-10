package cl.management.potion.service.implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import cl.management.potion.dto.request.UserRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.dto.response.UserResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.model.RoleEntity;
import cl.management.potion.model.UserEntity;
import cl.management.potion.repository.RoleRepository;
import cl.management.potion.repository.UserRepository;
import cl.management.potion.util.PasswordUtil;
import cl.management.potion.util.ValidationUtil;
import cl.management.potion.util.enums.ExceptionListEnum;
import cl.management.potion.util.enums.RoleEnum;

/**
 * Unit tests for UserServiceImpl using JUnit 5 and Mockito.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * 
 * @since 10-06-2026
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest validUserRequest;
    private UserEntity mockUser;
    private RoleEntity mockRole;

    @BeforeEach
    void setUp() {
        mockRole = RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .roleIcon("admin-icon")
                .build();

        mockUser = UserEntity.builder()
                .id(1L)
                .username("testuser")
                .password("hashedPassword123")
                .email("test@example.com")
                .role(mockRole)
                .build();
        mockUser.setActive(true);

        validUserRequest = new UserRequest();
        validUserRequest.setUsername("testuser");
        validUserRequest.setPassword("password123");
        validUserRequest.setEmail("test@example.com");
        validUserRequest.setRole(RoleEnum.ADMIN);
    }

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUserSuccess() throws ServiceException {
        // Arrange
        when(roleRepository.searchById(anyLong())).thenReturn(mockRole);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class);
                MockedStatic<PasswordUtil> mockedPassword = mockStatic(PasswordUtil.class)) {

            mockedValidation.when(() -> ValidationUtil.userRequestValidation(any(UserRequest.class)));
            mockedPassword.when(() -> PasswordUtil.passwordEncrypt(anyString())).thenReturn("hashedPassword123");
            when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

            // Act
            DefaultResponse response = userService.registerUser(validUserRequest);

            // Assert
            assertNotNull(response);
            assertEquals("201", response.getCode());
            verify(roleRepository).searchById(anyLong());
            verify(userRepository).save(any(UserEntity.class));
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when role not found during registration")
    void testRegisterUserRoleNotFound() {
        // Arrange
        when(roleRepository.searchById(anyLong())).thenReturn(null);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.userRequestValidation(any(UserRequest.class)));

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                userService.registerUser(validUserRequest);
            });

            assertEquals(ExceptionListEnum.ROLE_NOT_FOUND.getStatusCode(), exception.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should list users successfully")
    void testListUsersSuccess() throws ServiceException {
        // Arrange
        List<UserEntity> users = new ArrayList<>();
        users.add(mockUser);
        Page<UserEntity> userPage = new PageImpl<>(users, PageRequest.of(0, 20), 1);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()));

            // Act
            Page<UserResponse> response = userService.listUsers("validToken", PageRequest.of(0, 20));

            // Assert
            assertNotNull(response);
            assertEquals(1, response.getContent().size());
            verify(userRepository).findAll(any(PageRequest.class));
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when token validation fails during list")
    void testListUsersInvalidToken() {
        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()))
                    .thenThrow(new ServiceException(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR));

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                userService.listUsers("invalidToken", PageRequest.of(0, 20));
            });

            assertEquals(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR.getStatusCode(), exception.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should search user successfully")
    void testSearchUserSuccess() throws ServiceException {
        // Arrange
        when(userRepository.searchByUsername("testuser")).thenReturn(mockUser);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()));

            // Act
            DefaultResponse response = userService.searchUser("validToken", "testuser");

            // Assert
            assertNotNull(response);
            assertEquals("200", response.getCode());
            verify(userRepository).searchByUsername("testuser");
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when user not found during search")
    void testSearchUserNotFound() {
        // Arrange
        when(userRepository.searchByUsername("nonexistent")).thenReturn(null);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()));

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                userService.searchUser("validToken", "nonexistent");
            });

            assertEquals(ExceptionListEnum.USER_NOT_FOUND.getStatusCode(), exception.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUserSuccess() throws ServiceException {
        // Arrange
        when(userRepository.searchById(1L)).thenReturn(mockUser);
        when(roleRepository.searchById(anyLong())).thenReturn(mockRole);
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class);
                MockedStatic<PasswordUtil> mockedPassword = mockStatic(PasswordUtil.class)) {

            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()));
            mockedPassword.when(() -> PasswordUtil.passwordEncrypt(anyString())).thenReturn("hashedPassword123");

            // Act
            DefaultResponse response = userService.updateUser("validToken", 1L, validUserRequest);

            // Assert
            assertNotNull(response);
            assertEquals("200", response.getCode());
            verify(userRepository).searchById(1L);
            verify(userRepository).save(any(UserEntity.class));
        }
    }

    @Test
    @DisplayName("Should deactivate user successfully")
    void testDeactivateUserSuccess() throws ServiceException {
        // Arrange
        when(userRepository.searchById(1L)).thenReturn(mockUser);
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()));

            // Act
            userService.deactivateUser("validToken", 1L);

            // Assert
            verify(userRepository).searchById(1L);
            verify(userRepository).save(any(UserEntity.class));
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when deactivating user not found")
    void testDeactivateUserNotFound() {
        // Arrange
        when(userRepository.searchById(999L)).thenReturn(null);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()));

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                userService.deactivateUser("validToken", 999L);
            });

            assertEquals(ExceptionListEnum.USER_NOT_FOUND.getStatusCode(), exception.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should activate user successfully")
    void testActivateUserSuccess() throws ServiceException {
        // Arrange
        mockUser.setActive(false);
        when(userRepository.searchById(1L)).thenReturn(mockUser);
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()));

            // Act
            userService.activateUser("validToken", 1L);

            // Assert
            verify(userRepository).searchById(1L);
            verify(userRepository).save(any(UserEntity.class));
        }
    }
}