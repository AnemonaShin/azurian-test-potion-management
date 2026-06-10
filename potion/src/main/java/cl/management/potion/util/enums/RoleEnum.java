package cl.management.potion.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Util - Enums - Role Enum
 * 
 * Enum class for Roles inside DATABASE.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.0
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {
  ADMIN(1L),
  CRAFTER(2L),
  GARDENER(3L);

  private final Long id;
}
