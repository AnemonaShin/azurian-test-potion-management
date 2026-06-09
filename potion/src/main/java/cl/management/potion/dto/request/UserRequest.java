package cl.management.potion.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object - User Request
 * Request for the register/update of user data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

  @Valid
  private String username;

  @Valid
  private String password;

  @Valid
  private String email;

  @Valid
  private String address;
}
