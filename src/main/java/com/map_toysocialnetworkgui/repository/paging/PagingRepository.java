package com.map_toysocialnetworkgui.repository.paging;

import com.map_toysocialnetworkgui.model.entities.Entity;
import com.map_toysocialnetworkgui.repository.skeletons.CrudRepository;

/**
 * the interface for a generic paging repository
 *
 * @param <ID> - id of the entities from repository
 * @param <E>  - type of entities from repository
 */
public interface PagingRepository<ID, E extends Entity<ID>> extends CrudRepository<ID, E> {
    /**
     * returns all entities from a pageable entity
     *
     * @param pageable - said pageable entity
     * @return said entities
     */
    Page<E> findAll(Pageable pageable);
}
