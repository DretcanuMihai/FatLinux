package com.map_toysocialnetworkgui.repository;

import com.map_toysocialnetworkgui.model.entities.Entity;

/**
 * the interface of a generic repository for entities
 * @param <ID> - entity id type
 * @param <E> - entity type
 */
public interface Repository<ID,E extends Entity<ID>> {
    /**
     * saves an entity in the repo if no other entity with the same ID exists
     * @param e - said entity
     */
    void save(E e);

    /**
     * gets the entity with specified id from repo
     * @param id - said id
     * @return the entity if it exists, null otherwise
     */
    E get(ID id);

    /**
     * updates the entry in repo with the same id as an entity with said entity
     * @param e - said entity
     */
    void update(E e);

    /**
     * deletes the entity with the given id from the repository
     * @param id - said id
     */
    void delete(ID id);

    /**
     * gets all the entities in the repo
     * @return a collection of said entities
     */
    Iterable<E> getAll();
}
