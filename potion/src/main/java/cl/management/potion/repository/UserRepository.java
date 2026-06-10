package cl.management.potion.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cl.management.potion.model.UserEntity;

/**
 * User repository for access to user's data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    public UserEntity searchById(Long id);

    public UserEntity searchByUsername(String username);

    public Page<UserEntity> findAll(Pageable pageable);

}
