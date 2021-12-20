package com.map_toysocialnetworkgui.repository.skeletons.operation_based;

import com.map_toysocialnetworkgui.model.entities.Entity;
import com.map_toysocialnetworkgui.repository.CRUDException;

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
    E tryGet(ID id);

    /**
     * gets the entity with specified id from repo
     *
     * @param id - said id
     * @return said entity
     * @throws CRUDException if entity doesn't exist
     */
    default E get(ID id)throws CRUDException{
        E entity=tryGet(id);
        if(entity==null)
            throw new CRUDException("Error: Wanted entity doesn't exist;\n");
        return entity;
    }

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
    default boolean contains(ID id){
        return tryGet(id)!=null;
    }
}
