package cl.management.potion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model - Role Entity
 * 
 * Creates roles for users on the application.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.1
 */
@Entity
@Table(name = "Roles")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RoleEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long id;

  @Column(name = "role_name", nullable = false, unique = true)
  private String name;

  @Column(nullable = false, unique = true)
  private String roleIcon;
}
