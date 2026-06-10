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

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import cl.management.potion.dto.request.RoleRequest;
import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.dto.response.RoleResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.service.RoleService;
import cl.management.potion.util.enums.ExceptionListEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

/**
 * Unit tests for RoleController using JUnit 5 and Mockito.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RoleController Tests")
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    private RoleRequest validRoleRequest;
    private DefaultResponse successResponse;
    private String authToken;

    @BeforeEach
    void setUp() {
        validRoleRequest = new RoleRequest();
        validRoleRequest.setName("ADMIN");
        validRoleRequest.setRoleIcon("admin-icon");

        successResponse = DefaultResponse.builder()
                .code("200")
                .message("ROLE CREATED")
                .build();

        authToken = "Bearer validToken123";
    }

    @Test
    @DisplayName("Should create a new role successfully")
    void testCreateRoleSuccess() throws Exception {
        // Arrange
        when(roleService.createRole(anyString(), any(RoleRequest.class))).thenReturn(successResponse);

        // Act & Assert
        mockMvc.perform(post("/roles/")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRoleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("ROLE CREATED"));
    }

    @Test
    @DisplayName("Should return 400 when creating role without Authorization header")
    void testCreateRoleMissingAuthHeader() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/roles/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRoleRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when creating role with invalid data")
    void testCreateRoleInvalidData() throws Exception {
        // Arrange
        RoleRequest invalidRequest = new RoleRequest();
        invalidRequest.setName(null); // Invalid: null name
        when(roleService.createRole(anyString(), any(RoleRequest.class)))
                .thenThrow(new ServiceException(ExceptionListEnum.ROLE_REQUEST_NAME_ERROR));

        // Act & Assert
        mockMvc.perform(post("/roles/")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should list all roles successfully")
    void testListRolesSuccess() throws Exception {
        // Arrange
        var roleResponse = RoleResponse.builder()
                .id(1L).name("ADMIN").roleIcon("admin-icon").build();
        Page<RoleResponse> rolePage = new PageImpl<>(List.of(roleResponse), PageRequest.of(0, 10), 1);
        when(roleService.listRoles(anyString(), any(PageRequest.class))).thenReturn(rolePage);

        // Act & Assert
        mockMvc.perform(get("/roles/")
                .header("Authorization", authToken)
                .param("size", "10")
                .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("ADMIN"));
    }

    @Test
    @DisplayName("Should return 400 when listing roles without Authorization header")
    void testListRolesMissingAuthHeader() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/roles/")
                .param("size", "10")
                .param("page", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update role successfully")
    void testUpdateRoleSuccess() throws Exception {
        // Arrange
        when(roleService.updateRole(anyString(), anyLong(), any(RoleRequest.class)))
                .thenReturn(successResponse);

        // Act & Assert
        mockMvc.perform(put("/roles/1")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRoleRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent role")
    void testUpdateRoleNotFound() throws Exception {
        // Arrange
        when(roleService.updateRole(anyString(), anyLong(), any(RoleRequest.class)))
                .thenThrow(new ServiceException(ExceptionListEnum.ROLE_NOT_FOUND));

        // Act & Assert
        mockMvc.perform(put("/roles/999")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRoleRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should deactivate role successfully")
    void testDeactivateRoleSuccess() throws Exception {
        // Arrange
        doNothing().when(roleService).deactivateRole(anyString(), anyLong());

        // Act & Assert
        mockMvc.perform(delete("/roles/1")
                .header("Authorization", authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should activate role successfully")
    void testActivateRoleSuccess() throws Exception {
        // Arrange
        doNothing().when(roleService).activateRole(anyString(), anyLong());

        // Act & Assert
        mockMvc.perform(patch("/roles/1/activate")
                .header("Authorization", authToken))
                .andExpect(status().isNoContent());
    }
}
