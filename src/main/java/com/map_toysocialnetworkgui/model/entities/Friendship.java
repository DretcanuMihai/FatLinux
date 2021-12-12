package com.map_toysocialnetworkgui.model.entities;

import com.map_toysocialnetworkgui.utils.structures.UnorderedPair;

import java.time.LocalDate;
import java.util.Objects;

/**
 * class that defines friendship entity
 */
public class Friendship extends Entity<UnorderedPair<String>> {
    /**
     * entity begin date
     */
    private final LocalDate beginDate;

    /**
     * creates a friendship for two users identified by email
     * friendship started on beginDate
     *
     * @param email1    - first user's email
     * @param email2    - second user's email
     * @param beginDate - the date the friendship begun
     */
    public Friendship(String email1, String email2, LocalDate beginDate) {
        super(new UnorderedPair<>(email1, email2));
        this.beginDate = beginDate;
    }

    /**
     * gets the friendship begin date
     *
     * @return said date
     */
    public LocalDate getBeginDate() {
        return beginDate;
    }

    /**
     * gets the users' emails
     *
     * @return users' emails
     */
    public UnorderedPair<String> getEmails() {
        return getId();
    }

    /**
     * verifies if instance is equal to an object
     * instance is equal with object if they are of same type and have the same attributes
     *
     * @param o - the object with which we verify equality
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Friendship friendship)) return false;
        return Objects.equals(getEmails(), friendship.getEmails()) &&
                Objects.equals(beginDate, friendship.beginDate);

    }

    /**
     * calculates the hashcode of the instance based on all the attributes
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(beginDate, getEmails());
    }
}
