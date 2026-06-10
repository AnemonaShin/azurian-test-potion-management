package cl.management.potion.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cl.management.potion.dto.response.ExceptionResponse;

/**
 * Global Exception Handler for controlling erros and redirect them.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.1
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @SuppressWarnings("null")
    public ResponseEntity<ExceptionResponse> handleGlobalException(ServiceException serviceException) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                .code(serviceException.getCode()).message(serviceException.getMessage()).build(),
                serviceException.getStatusCode());
    }

}
