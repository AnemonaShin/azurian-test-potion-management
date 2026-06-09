package cl.management.potion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model - Item Entity
 * 
 * Item model designed to make users create sort of things to make potions.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@Entity
@Table(name = "Items", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Long id;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "username", nullable = false)
  private UserEntity createdBy;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

}
