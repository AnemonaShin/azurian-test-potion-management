package cl.management.potion.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cl.management.potion.model.ItemEntity;

/**
 * Item Repository for access to item's data.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 08-06-2026
 * @version 1.0.0
 */
@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {

}
