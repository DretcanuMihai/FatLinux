package com.map_toysocialnetworkgui.repository.skeletons;

import com.map_toysocialnetworkgui.model.entities.Entity;
import com.map_toysocialnetworkgui.repository.CRUDException;

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
     * @throws CRUDException if an entity identified by the same ID doesn't already exist
     */
    void update(E e) throws CRUDException;
}
