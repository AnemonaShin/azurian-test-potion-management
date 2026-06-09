package cl.management.potion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model - User Entity
 * 
 * User data model for the usage in the application.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@Entity
@Table(name = "Users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "role_id")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String address;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id", nullable = false)
  private RoleEntity roleId;

  @Column(nullable = false)
  private boolean active;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

}
