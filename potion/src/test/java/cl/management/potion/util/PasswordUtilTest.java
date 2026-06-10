package cl.management.potion.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for PasswordUtil using JUnit 5.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 10-06-2026
 * @version 1.0.0
 */
@DisplayName("PasswordUtil Tests")
class PasswordUtilTest {

    @Test
    @DisplayName("Should encrypt password successfully")
    void testPasswordEncryptSuccess() {
        // Arrange
        String password = "mySecurePassword123";

        // Act
        String encryptedPassword = PasswordUtil.passwordEncrypt(password);

        // Assert
        assertNotNull(encryptedPassword);
        assertNotEquals(password, encryptedPassword);
    }

    @Test
    @DisplayName("Should encrypt same password differently each time")
    void testPasswordEncryptDifferentEachTime() {
        // Arrange
        String password = "mySecurePassword123";

        // Act
        String encrypted1 = PasswordUtil.passwordEncrypt(password);
        String encrypted2 = PasswordUtil.passwordEncrypt(password);

        // Assert - BCrypt generates different hashes each time
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    @DisplayName("Should verify correct password match")
    void testPasswordDecryptSuccess() {
        // Arrange
        String plainPassword = "mySecurePassword123";
        String encryptedPassword = PasswordUtil.passwordEncrypt(plainPassword);

        // Act
        Boolean matches = PasswordUtil.passwordDecrypt(plainPassword, encryptedPassword);

        // Assert
        assertTrue(matches);
    }

    @Test
    @DisplayName("Should fail when password does not match")
    void testPasswordDecryptFail() {
        // Arrange
        String plainPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        String encryptedPassword = PasswordUtil.passwordEncrypt(plainPassword);

        // Act
        Boolean matches = PasswordUtil.passwordDecrypt(wrongPassword, encryptedPassword);

        // Assert
        assertFalse(matches);
    }

    @Test
    @DisplayName("Should handle complex passwords")
    void testPasswordEncryptComplexPassword() {
        // Arrange
        String complexPassword = "Qw3!@#$%^&*()_+-=[]{}|;:',.<>?/";

        // Act
        String encryptedPassword = PasswordUtil.passwordEncrypt(complexPassword);
        Boolean matches = PasswordUtil.passwordDecrypt(complexPassword, encryptedPassword);

        // Assert
        assertNotNull(encryptedPassword);
        assertTrue(matches);
    }

    @Test
    @DisplayName("Should handle long passwords")
    void testPasswordEncryptLongPassword() {
        // Arrange
        String longPassword = "ThisIsALongPasswordWithManyCharsToTestEncryptionMethod!";

        // Act
        String encryptedPassword = PasswordUtil.passwordEncrypt(longPassword);
        Boolean matches = PasswordUtil.passwordDecrypt(longPassword, encryptedPassword);

        // Assert
        assertNotNull(encryptedPassword);
        assertTrue(matches);
    }

    @Test
    @DisplayName("Should not match empty password with non-empty encrypted password")
    void testPasswordDecryptEmptyPassword() {
        // Arrange
        String plainPassword = "password123";
        String encryptedPassword = PasswordUtil.passwordEncrypt(plainPassword);

        // Act
        Boolean matches = PasswordUtil.passwordDecrypt("", encryptedPassword);

        // Assert
        assertFalse(matches);
    }

    @Test
    @DisplayName("Should be case-sensitive")
    void testPasswordEncryptCaseSensitive() {
        // Arrange
        String password1 = "Password123";
        String password2 = "password123";
        String encryptedPassword1 = PasswordUtil.passwordEncrypt(password1);

        // Act
        Boolean matches = PasswordUtil.passwordDecrypt(password2, encryptedPassword1);

        // Assert
        assertFalse(matches);
    }
}
