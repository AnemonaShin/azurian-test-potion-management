package cl.management.potion.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import cl.management.potion.util.enums.ExceptionListEnum;
import lombok.Getter;

/**
 * Custom service exception to catch errors inside the application.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.1
 */
@Getter
public class ServiceException extends Exception {

  final HttpStatus statusCode;
  final String code;
  final String message;
  final LocalDateTime timestamp;

  public ServiceException(ExceptionListEnum exceptionListEnum) {
    this.statusCode = exceptionListEnum.getStatusCode();
    this.code = String.valueOf(exceptionListEnum.getStatusCode().value());
    this.message = exceptionListEnum.getMessage();
    this.timestamp = LocalDateTime.now();
  }

  public ServiceException(HttpStatus statusCode, String message) {
    this.statusCode = statusCode;
    this.code = String.valueOf(statusCode.value());
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

  public ServiceException(HttpStatus statusCode, String code, String message) {
    this.statusCode = statusCode;
    this.code = code;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

}
