package com.map_toysocialnetworkgui.model.validators;

/**
 * the interface for a generic Validator for entities
 * @param <T> - entities' type
 */
public interface Validator<T> {
    /**
     * validates an entity
     * @param entity - said entity
     * @throws ValidationException if the entity isn't valid
     */
    void validate(T entity) throws ValidationException;

}
