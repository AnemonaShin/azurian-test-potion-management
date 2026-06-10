package cl.management.potion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

/**
 * Model - Base Entity
 * 
 * Father class who bring heredity to others entitys.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.0
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private boolean active;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();

    this.createdAt = now;
    this.updatedAt = now;
    this.active = true;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
