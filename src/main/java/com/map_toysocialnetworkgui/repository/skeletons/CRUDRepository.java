package com.map_toysocialnetworkgui.repository.skeletons;

import com.map_toysocialnetworkgui.model.entities.Entity;

/**
 * the interface of a generic repository for entities
 * contains all CRUD operations
 *
 * @param <ID> - entity id type
 * @param <E>  - entity type
 */
public interface CRUDRepository<ID, E extends Entity<ID>> extends CreateOperationRepository<ID, E>,
        ReadOperationRepository<ID, E>, UpdateOperationRepository<ID, E>, DeleteOperationRepository<ID, E> {
}
