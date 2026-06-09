package cl.management.potion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.management.potion.model.RoleEntity;

/**
 * Role repository for access to role's data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

}
