package cl.management.potion.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cl.management.potion.model.RoleEntity;
import cl.management.potion.util.enums.RoleEnum;

/**
 * Role repository for access to role's data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    public RoleEntity searchById(Long id);

    public RoleEntity searchById(RoleEnum role);

    public Page<RoleEntity> findAll(Pageable pageable);
}
