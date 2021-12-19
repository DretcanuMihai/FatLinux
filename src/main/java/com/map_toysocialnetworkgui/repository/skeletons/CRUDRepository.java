package com.map_toysocialnetworkgui.repository.skeletons;

import com.map_toysocialnetworkgui.model.entities.Entity;
import com.map_toysocialnetworkgui.repository.CRUDException;

/**
 * the interface of a generic repository for entities
 *
 * @param <ID> - entity id type
 * @param <E>  - entity type
 */
public interface Repository<ID, E extends Entity<ID>>{
    /**
     * saves an entity in the repo if no other entity with the same ID exists
     *
     * @param e - said entity
     * @throws CRUDException if an entity identified by the same ID already exists
     */
    void create(E e) throws CRUDException;

    /**
     * gets the entity with specified id from repo
     *
     * @param id - said id
     * @return the entity if it exists, null otherwise
     */
    E read(ID id);

    /**
     * updates the entry in repo with the same id as an entity with said entity
     *
     * @param e - said entity
     * @throws CRUDException if an entity identified by the same ID doesn't already exist
     */
    void update(E e) throws CRUDException;

    /**
     * deletes the entity with the given id from the repository
     *
     * @param id - said id
     * @throws CRUDException if an entity identified by the same ID doesn't already exist
     */
    void delete(ID id) throws CRUDException;

    /**
     * gets all the entities in the repo
     *
     * @return a collection of said entities
     */
    Iterable<E> getAll();
}
