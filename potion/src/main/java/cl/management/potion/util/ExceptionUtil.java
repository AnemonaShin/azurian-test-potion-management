package cl.management.potion.util;

import org.springframework.dao.DataIntegrityViolationException;

import cl.management.potion.exception.ServiceException;
import cl.management.potion.util.enums.ExceptionListEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Util - ExceptionUtil
 * 
 * Exception Util for mapping exceptions.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.0
 */
@UtilityClass
@Slf4j
public class ExceptionUtil {

  /**
   * Method that validates Data Integrity Violation Exceptions.
   * 
   * @param divx Exception Object
   * @return Exception List with Certain error for Custom Exception and Handler.
   * @throws ServiceException Custom Exception Object.
   */
  public final ExceptionListEnum dataIntegrityErrors(DataIntegrityViolationException divx) throws ServiceException {
    switch (divx.getMessage()) {
      case String username when username.contains("(username)"):
        log.error("USER USERNAME DUPLICATION ERROR");
        return ExceptionListEnum.USER_USERNAME_ALREADY_EXIST;
      case String email when email.contains("(email)"):
        log.error("USER EMAIL DUPLICATION ERROR");
        return ExceptionListEnum.USER_EMAIL_ALREADY_EXIST;
      case String roleName when roleName.contains("(role_name)"):
        log.error("ROLE ROLE NAME DUPLICATION ERROR");
        return ExceptionListEnum.ROLE_NAME_ALREADY_EXIST;
      case String roleIcon when roleIcon.contains("(role_icon)"):
        log.error("ROLE ROLE ICON DUPLICATION ERROR");
        return ExceptionListEnum.ROLE_ICON_ALREADY_EXIST;
      default:
        return ExceptionListEnum.DEFAULT_DATA_INTEGRITY_ERROR;
    }
  }

}
