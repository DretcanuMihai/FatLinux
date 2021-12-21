package com.map_toysocialnetworkgui.repository.skeletons.operation_based;

import com.map_toysocialnetworkgui.model.entities.Entity;

/**
 * interface for repository which has a create operation for a specific entity
 *
 * @param <ID> - entity id type
 * @param <E>  - entity type
 */
public interface CreateOperationRepository<ID, E extends Entity<ID>> {
    /**
     * saves an entity in the repo if no other entity with the same ID exists
     *
     * @param e - said entity
     * @throws AdministrationException if an entity identified by the same ID already exists
     */
    void save(E e) throws AdministrationException;
}
