package com.map_toysocialnetworkgui.repository.skeletons;

import com.map_toysocialnetworkgui.model.entities.Entity;

/**
 * the interface of a generic repository for entities
 * contains all CRUD operations
 *
 * @param <ID> - entity id type
 * @param <E>  - entity type
 */
public interface CRUDRepository<ID, E extends Entity<ID>> {

    /**
     * saves an entity in the repo
     *
     * @param e - said entity
     * @return true if the operation was successful, false otherwise
     */
    E save(E e);

    /**
     * gets the entity with specified id from repo
     *
     * @param id - said id
     * @return the entity if it exists, null otherwise
     */
    E findOne(ID id);

    /**
     * gets all the entities in the repo
     *
     * @return a collection of said entities
     */
    Iterable<E> findAll();

    /**
     * updates the entry in repo with the same id as an entity with said entity
     *
     * @param e - said entity
     * @return true if the operation was successful, false otherwise
     */
    E update(E e);

    /**
     * deletes the entity with the given id from the repository
     *
     * @param id - said id
     * @return true if the operation was successful, false otherwise
     */
    E delete(ID id);
}
