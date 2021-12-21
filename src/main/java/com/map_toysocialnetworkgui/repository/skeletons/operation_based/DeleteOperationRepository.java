package com.map_toysocialnetworkgui.repository.skeletons.operation_based;

import com.map_toysocialnetworkgui.model.entities.Entity;

/**
 * interface for repository which has a create operation for a specific entity
 *
 * @param <ID> - entity id type
 * @param <E>  - entity type
 */
public interface DeleteOperationRepository<ID, E extends Entity<ID>> {
    /**
     * deletes the entity with the given id from the repository
     *
     * @param id - said id
     * @return true if the operation was successful, false otherwise
     */
    boolean delete(ID id);
}
