package com.map_toysocialnetworkgui.repository.skeletons.operation_based;

import com.map_toysocialnetworkgui.model.entities.Entity;

/**
 * interface for repository which has an update operation for a specific entity
 *
 * @param <ID> - entity id type
 * @param <E>  - entity type
 */
public interface UpdateOperationRepository<ID, E extends Entity<ID>> {
    /**
     * updates the entry in repo with the same id as an entity with said entity
     *
     * @param e - said entity
     * @return true if the operation was successful, false otherwise
     */
    boolean update(E e);
}
