package cl.management.potion.dto.request;

import cl.management.potion.util.enums.RoleEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object - User Request
 * Request for the register/update of user data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

  @NotNull
  @NotEmpty
  private String username;

  @NotNull
  @NotEmpty
  private String password;

  @NotNull
  @NotEmpty
  private String email;

  @NotNull
  private RoleEnum role;
}
