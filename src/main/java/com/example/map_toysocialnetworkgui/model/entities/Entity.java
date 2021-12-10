package com.example.map_toysocialnetworkgui.model.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * generic entity type identified by an id
 *
 * @param <ID> - said id
 */
public class Entity<ID> implements Serializable {
    /**
     * entity id
     */
    private final ID id;

    /**
     * creates entity with an id
     *
     * @param id - said id
     */
    public Entity(ID id) {
        this.id = id;
    }

    /**
     * gets entity ID
     *
     * @return entity's id
     */
    public ID getId() {
        return id;
    }

    /**
     * verifies if instance is equal to other object
     * they are equal if they are both of the same type and have the same id
     *
     * @param o - the object with which we verify equality
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Entity<?> entity))
            return false;
        return Objects.equals(id, entity.id);
    }

    /**
     * calculates object hashcode
     *
     * @return hashcode (based on ID)
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}
