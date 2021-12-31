package com.map_toysocialnetworkgui.utils.events;

/**
 * class that describes an event for a modification of an entity
 *
 * @param <ID> - the entity's id type
 */
public class EntityModificationEvent<ID> implements Event {
    private final ChangeEventType type;
    private final ID modifiedEntityID;

    /**
     * create an event
     *
     * @param type             - event's type
     * @param modifiedEntityID - the id of the entity modified
     */
    public EntityModificationEvent(ChangeEventType type, ID modifiedEntityID) {
        this.type = type;
        this.modifiedEntityID = modifiedEntityID;
    }

    /**
     * returns the type of the event
     *
     * @return - said type
     */
    public ChangeEventType getType() {
        return type;
    }

    /**
     * returns the id of the modified entity
     *
     * @return - said id
     */
    public ID getModifiedEntityID() {
        return modifiedEntityID;
    }
}
