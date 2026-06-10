package cl.management.potion.util;

import java.util.Base64;

import cl.management.potion.dto.request.RoleRequest;
import cl.management.potion.dto.request.UserRequest;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.util.enums.ExceptionListEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Util - ValidationUtil
 * 
 * Validation Util for validate request data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.0
 */
@UtilityClass
@Slf4j
public class ValidationUtil {

  /**
   * Method that validate not null values.
   * 
   * @param value             Value wanted to validate.
   * @param exceptionListEnum Error wanted to throw.
   * @throws ServiceException Custom exception for errors and handler.
   */
  public static void validateNotNull(Object value, ExceptionListEnum exceptionListEnum) throws ServiceException {
    if (value == null) {
      log.error("{}", exceptionListEnum.getMessage());
      throw new ServiceException(exceptionListEnum);
    }
  }

  /**
   * Method that validate not empty values.
   * 
   * @param value             Value wanted to validate.
   * @param exceptionListEnum Error wanted to throw.
   * @throws ServiceException Custom exception for errors and handler.
   */
  public static void validateNotEmpty(String value, ExceptionListEnum exceptionListEnum) throws ServiceException {
    if (value == null || value.trim().isEmpty()) {
      log.error("{}", exceptionListEnum.getMessage());
      throw new ServiceException(exceptionListEnum);
    }
  }

  /**
   * Method that validate Request for user pourposes.
   * 
   * @param userBody Request for user.
   * @throws ServiceException Custom exception for errors and handler.
   */
  public static void userRequestValidation(UserRequest userBody) throws ServiceException {
    log.info("VALIDATING REQUEST");
    validateNotNull(userBody, ExceptionListEnum.DEFAULT_REQUEST_NULL_ERROR);
    log.info("VALIDATING USERNAME");
    validateNotEmpty(userBody.getUsername(), ExceptionListEnum.USER_REQUEST_USERNAME_ERROR);
    log.info("VALIDATING PASSWORD");
    validateNotEmpty(userBody.getPassword(), ExceptionListEnum.USER_REQUEST_PASSWORD_ERROR);
    log.info("VALIDATING EMAIL");
    validateNotEmpty(userBody.getEmail(), ExceptionListEnum.USER_REQUEST_EMAIL_ERROR);
    log.info("VALIDATING ROLE");
    validateNotEmpty(userBody.getRole().toString(), ExceptionListEnum.USER_REQUEST_ROLE_ERROR);
  }

  /**
   * Method for Role Request Validation
   * 
   * @param roleBody Request to validate.
   * @throws ServiceException Custom exception for errors and handler.
   */
  public static void roleRequestValidation(RoleRequest roleBody) throws ServiceException {

    log.info("VALIDATING REQUEST");
    validateNotNull(roleBody, ExceptionListEnum.DEFAULT_REQUEST_NULL_ERROR);
    log.info("VALIDATING NAME");
    validateNotEmpty(roleBody.getName(), ExceptionListEnum.ROLE_REQUEST_NAME_ERROR);
    log.info("VALIDATING ROLE ICON");
    validateNotEmpty(roleBody.getRoleIcon(), ExceptionListEnum.ROLE_REQUEST_ICON_ERROR);
  }

  /**
   * Method that validate an existing token.
   * 
   * @param token Token to validate.
   * @throws ServiceException Custom exception for errors and handler.
   */
  public static void tokenValidation(String token) throws ServiceException {
    validateNotEmpty(token, ExceptionListEnum.USER_REQUEST_PASSWORD_ERROR);

    try {
      token = token.replace("Bearer ", "");

      var tokenDecoded = new String(Base64.getDecoder().decode(token));

      tokenDecoded = new String(Base64.getDecoder().decode(tokenDecoded));

      var splitedToken = tokenDecoded.split(":");

      if (Boolean.TRUE.equals(PasswordUtil.passwordDecrypt(splitedToken[0], splitedToken[1]))) {
        log.info("VALIDATION OK");
      } else {
        throw new ServiceException(ExceptionListEnum.USER_PASSWORD_INCORRECT);
      }
    } catch (Exception ex) {
      throw new ServiceException(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR);
    }

  }

  /**
   * Method that validate an existing token.
   * Charge of TokenValidation
   * This one validates token for the generation of it.
   * 
   * @param token        Token to validate.
   * @param userPassword User Password to matchup.
   * @param dbPassword   DB Password to matchup.
   * @throws ServiceException Custom exception for errors and handler.
   */
  public static void tokenValidation(String token, String userPassword, String dbPassword) throws ServiceException {

    validateNotEmpty(token, ExceptionListEnum.USER_REQUEST_PASSWORD_ERROR);
    try {
      if (Boolean.TRUE.equals(PasswordUtil.passwordDecrypt(userPassword, dbPassword))) {
        log.info("VALIDATION OK");
      } else {
        throw new ServiceException(ExceptionListEnum.USER_PASSWORD_INCORRECT);
      }
    } catch (Exception ex) {
      throw new ServiceException(ExceptionListEnum.DEFAULT_AUTHORIZATION_ERROR);
    }
  }
}
