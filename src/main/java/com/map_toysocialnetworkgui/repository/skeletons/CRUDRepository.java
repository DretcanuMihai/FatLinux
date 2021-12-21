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
    boolean save(E e);

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
    Iterable<E> getAll();

    /**
     * checks if an entity with a specified id exists in repo
     *
     * @param id - said id
     * @return true if it exists, false otherwise
     */
    default boolean contains(ID id) {
        return get(id) != null;
    }

    /**
     * updates the entry in repo with the same id as an entity with said entity
     *
     * @param e - said entity
     * @return true if the operation was successful, false otherwise
     */
    boolean update(E e);

    /**
     * deletes the entity with the given id from the repository
     *
     * @param id - said id
     * @return true if the operation was successful, false otherwise
     */
    boolean delete(ID id);
}
