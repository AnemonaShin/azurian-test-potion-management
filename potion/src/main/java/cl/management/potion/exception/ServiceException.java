package cl.management.potion.exception;

import java.time.LocalDateTime;

/**
 * Custom service exception to catch errors inside the application.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
public class ServiceException extends Exception {

  final String code;
  final String message;
  final String description;
  final LocalDateTime timestamp;

  public ServiceException(String code, String message) {
    this.code = code;
    this.message = message;
    this.description = super.getMessage();
    this.timestamp = LocalDateTime.now();
  }

}
