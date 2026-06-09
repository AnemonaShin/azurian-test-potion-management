package cl.management.potion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object - Default Response
 * Default API Response.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DefaultResponse {

  private String code;

  private String message;

  private Object response;
}
