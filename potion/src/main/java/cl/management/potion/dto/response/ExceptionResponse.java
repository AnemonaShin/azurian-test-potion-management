package cl.management.potion.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object - Exception Response
 * Exception response for system error responses.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ExceptionResponse {

    String code;

    String message;

    LocalDateTime timestamp;
}
