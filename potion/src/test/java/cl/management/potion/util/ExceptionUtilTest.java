package cl.management.potion.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import cl.management.potion.exception.ServiceException; // Asegúrate de tener este import
import cl.management.potion.util.enums.ExceptionListEnum;

/**
 * Unit tests for ExceptionUtil using JUnit 5.
 * * @author Christian Ramirez (cramireza1997@gmail.com)
 * 
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("ExceptionUtil Tests")
class ExceptionUtilTest {

    @Test
    @DisplayName("Should map DataIntegrityViolationException to USER_USERNAME_ALREADY_EXIST")
    void testDataIntegrityErrorsUsernameDuplication() throws ServiceException { // 👈 AGREGADO
        // Arrange
        DataIntegrityViolationException exception = mock(DataIntegrityViolationException.class);
        when(exception.getMessage()).thenReturn("Duplicate entry for (username)");

        // Act
        ExceptionListEnum result = ExceptionUtil.dataIntegrityErrors(exception);

        // Assert
        assertEquals(ExceptionListEnum.USER_USERNAME_ALREADY_EXIST, result);
    }

    @Test
    @DisplayName("Should map DataIntegrityViolationException to USER_EMAIL_ALREADY_EXIST")
    void testDataIntegrityErrorsEmailDuplication() throws ServiceException { // 👈 AGREGADO
        // Arrange
        DataIntegrityViolationException exception = mock(DataIntegrityViolationException.class);
        when(exception.getMessage()).thenReturn("Duplicate entry for (email)");

        // Act
        ExceptionListEnum result = ExceptionUtil.dataIntegrityErrors(exception);

        // Assert
        assertEquals(ExceptionListEnum.USER_EMAIL_ALREADY_EXIST, result);
    }

    @Test
    @DisplayName("Should map DataIntegrityViolationException to ROLE_NAME_ALREADY_EXIST")
    void testDataIntegrityErrorsRoleNameDuplication() throws ServiceException { // 👈 AGREGADO
        // Arrange
        DataIntegrityViolationException exception = mock(DataIntegrityViolationException.class);
        when(exception.getMessage()).thenReturn("Duplicate entry for (role_name)");

        // Act
        ExceptionListEnum result = ExceptionUtil.dataIntegrityErrors(exception);

        // Assert
        assertEquals(ExceptionListEnum.ROLE_NAME_ALREADY_EXIST, result);
    }

    @Test
    @DisplayName("Should map DataIntegrityViolationException to ROLE_ICON_ALREADY_EXIST")
    void testDataIntegrityErrorsRoleIconDuplication() throws ServiceException { // 👈 AGREGADO
        // Arrange
        DataIntegrityViolationException exception = mock(DataIntegrityViolationException.class);
        when(exception.getMessage()).thenReturn("Duplicate entry for (role_icon)");

        // Act
        ExceptionListEnum result = ExceptionUtil.dataIntegrityErrors(exception);

        // Assert
        assertEquals(ExceptionListEnum.ROLE_ICON_ALREADY_EXIST, result);
    }

    @Test
    @DisplayName("Should map unknown DataIntegrityViolationException to DEFAULT_DATA_INTEGRITY_ERROR")
    void testDataIntegrityErrorsUnknown() throws ServiceException { // 👈 AGREGADO
        // Arrange
        DataIntegrityViolationException exception = mock(DataIntegrityViolationException.class);
        when(exception.getMessage()).thenReturn("Unknown database error");

        // Act
        ExceptionListEnum result = ExceptionUtil.dataIntegrityErrors(exception);

        // Assert
        assertEquals(ExceptionListEnum.DEFAULT_DATA_INTEGRITY_ERROR, result);
    }

    @Test
    @DisplayName("Should map null message to DEFAULT_DATA_INTEGRITY_ERROR")
    void testDataIntegrityErrorsNullMessage() throws ServiceException { // 👈 AGREGADO
        // Arrange
        DataIntegrityViolationException exception = mock(DataIntegrityViolationException.class);
        when(exception.getMessage()).thenReturn(null);

        // Act
        ExceptionListEnum result = ExceptionUtil.dataIntegrityErrors(exception);

        // Assert
        assertEquals(ExceptionListEnum.DEFAULT_DATA_INTEGRITY_ERROR, result);
    }
}