package com.map_toysocialnetworkgui.repository.skeletons.operation_based;

import com.map_toysocialnetworkgui.model.entities.Entity;

import java.util.Collection;

/**
 * interface for repository which has a read operation for a specific entity
 *
 * @param <ID> - entity id type
 * @param <E>  - entity type
 */
public interface ReadOperationRepository<ID, E extends Entity<ID>> {
    /**
     * gets the entity with specified id from repo
     *
     * @param id - said id
     * @return the entity if it exists, null otherwise
     */
    E get(ID id);

    /**
     * gets all the entities in the repo
     *
     * @return a collection of said entities
     */
    Collection<E> getAll();

    /**
     * checks if an entity with a specified id exists in repo
     *
     * @param id - said id
     * @return true if it exists, false otherwise
     */
    default boolean contains(ID id) {
        return get(id) != null;
    }
}
