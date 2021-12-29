package com.map_toysocialnetworkgui.model.validators;

/**
 * the interface for a generic validator for entities
 *
 * @param <T> - entities' type
 */
public interface Validator<T> {
    /**
     * validates an entity (default validation - that is for a save)
     *
     * @param entity - said entity
     * @throws ValidationException if the entity isn't valid
     */
    void validateDefault(T entity) throws ValidationException;
}
