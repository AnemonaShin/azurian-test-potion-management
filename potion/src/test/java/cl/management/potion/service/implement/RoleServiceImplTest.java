package cl.management.potion.service.implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import cl.management.potion.dto.request.RoleRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.dto.response.RoleResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.model.RoleEntity;
import cl.management.potion.repository.RoleRepository;
import cl.management.potion.util.ValidationUtil;
import cl.management.potion.util.enums.ExceptionListEnum;

/**
 * Unit tests for RoleServiceImpl using JUnit 5 and Mockito.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * 
 * @since 10-06-2026
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RoleServiceImpl Tests")
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleRequest validRoleRequest;
    private RoleEntity mockRole;

    @BeforeEach
    void setUp() {
        mockRole = RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .roleIcon("admin-icon")
                .build();
        mockRole.setActive(true);

        validRoleRequest = new RoleRequest();
        validRoleRequest.setName("ADMIN");
        validRoleRequest.setRoleIcon("admin-icon");
    }

    @Test
    @DisplayName("Should create role successfully")
    void testCreateRoleSuccess() throws ServiceException {
        // Arrange
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(mockRole);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString())).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.roleRequestValidation(any(RoleRequest.class))).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.validateNotNull(any(), any())).thenCallRealMethod();

            // Act
            DefaultResponse response = roleService.createRole("validToken", validRoleRequest);

            // Assert
            assertNotNull(response);
            assertEquals("200", response.getCode());
            assertEquals("ROLE REGISTERED", response.getMessage());
            verify(roleRepository).save(any(RoleEntity.class));
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when token validation fails")
    void testCreateRoleInvalidToken() {
        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {

            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()))
                    .thenThrow(new ServiceException(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR));

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                roleService.createRole("invalidToken", validRoleRequest);
            });

            assertEquals(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR.getStatusCode(), exception.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should list roles successfully")
    void testListRolesSuccess() throws ServiceException {
        // Arrange
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(mockRole);
        Page<RoleEntity> rolePage = new PageImpl<>(roles, PageRequest.of(0, 10), 1);

        when(roleRepository.findAll(any(PageRequest.class))).thenReturn(rolePage);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString())).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.validateNotNull(any(), any())).thenCallRealMethod();

            // Act
            Page<RoleResponse> response = roleService.listRoles("validToken", PageRequest.of(0, 10));

            // Assert
            assertNotNull(response);
            assertEquals(1, response.getContent().size());
            verify(roleRepository).findAll(any(PageRequest.class));
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when token validation fails during list")
    void testListRolesInvalidToken() {
        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString()))
                    .thenThrow(new ServiceException(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR));

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                roleService.listRoles("invalidToken", PageRequest.of(0, 10));
            });

            assertEquals(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR.getStatusCode(), exception.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should update role successfully")
    void testUpdateRoleSuccess() throws ServiceException {
        // Arrange
        when(roleRepository.searchById(1L)).thenReturn(mockRole);
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(mockRole);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString())).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.roleRequestValidation(any(RoleRequest.class))).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.validateNotNull(any(), any())).thenCallRealMethod();

            // Act
            DefaultResponse response = roleService.updateRole("validToken", 1L, validRoleRequest);

            // Assert
            assertNotNull(response);
            assertEquals("200 OK", response.getCode());
            verify(roleRepository).searchById(1L);
            verify(roleRepository).save(any(RoleEntity.class));
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when role not found during update")
    void testUpdateRoleNotFound() {
        // Arrange
        when(roleRepository.searchById(999L)).thenReturn(null);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString())).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.roleRequestValidation(any(RoleRequest.class))).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.validateNotNull(any(), any())).thenCallRealMethod();

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                roleService.updateRole("validToken", 999L, validRoleRequest);
            });

            assertEquals(ExceptionListEnum.ROLE_NOT_FOUND.getStatusCode(), exception.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should deactivate role successfully")
    void testDeactivateRoleSuccess() throws ServiceException {
        // Arrange
        when(roleRepository.searchById(1L)).thenReturn(mockRole);
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(mockRole);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString())).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.validateNotNull(any(), any())).thenCallRealMethod();

            // Act
            roleService.deactivateRole("validToken", 1L);

            // Assert
            verify(roleRepository).searchById(1L);
            verify(roleRepository).save(any(RoleEntity.class));
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when role not found during deactivate")
    void testDeactivateRoleNotFound() {
        // Arrange
        when(roleRepository.searchById(999L)).thenReturn(null);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString())).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.validateNotNull(any(), any())).thenCallRealMethod();

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                roleService.deactivateRole("validToken", 999L);
            });

            assertEquals(ExceptionListEnum.ROLE_NOT_FOUND.getStatusCode(), exception.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should activate role successfully")
    void testActivateRoleSuccess() throws ServiceException {
        // Arrange
        mockRole.setActive(false);
        when(roleRepository.searchById(1L)).thenReturn(mockRole);
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(mockRole);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString())).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.validateNotNull(any(), any())).thenCallRealMethod();

            // Act
            roleService.activateRole("validToken", 1L);

            // Assert
            verify(roleRepository).searchById(1L);
            verify(roleRepository).save(any(RoleEntity.class));
        }
    }

    @Test
    @DisplayName("Should throw ServiceException when role not found during activate")
    void testActivateRoleNotFound() {
        // Arrange
        when(roleRepository.searchById(999L)).thenReturn(null);

        try (MockedStatic<ValidationUtil> mockedValidation = mockStatic(ValidationUtil.class)) {
            mockedValidation.when(() -> ValidationUtil.tokenValidation(anyString())).thenAnswer(i -> null);
            mockedValidation.when(() -> ValidationUtil.validateNotNull(any(), any())).thenCallRealMethod();

            // Act & Assert
            ServiceException exception = assertThrows(ServiceException.class, () -> {
                roleService.activateRole("validToken", 999L);
            });

            assertEquals(ExceptionListEnum.ROLE_NOT_FOUND.getStatusCode(), exception.getStatusCode());
        }
    }
}