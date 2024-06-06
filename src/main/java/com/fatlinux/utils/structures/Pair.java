package com.fatlinux.utils.structures;

import java.util.Objects;

/**
 * class whose instances represent the concept of pair
 * in a normal pair, the order of the elements is important
 * the elements may have different typing
 *
 * @param <T1> - first element type
 * @param <T2> - second element type
 */
public class Pair<T1, T2> {
    /**
     * first entity
     */
    private T1 t1;

    /**
     * second entity
     */
    private T2 t2;

    /**
     * constructs a pair of two objects
     *
     * @param t1 - first object
     * @param t2 - second object
     */
    public Pair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    /**
     * get the first element of the pair
     *
     * @return said element
     */
    public T1 getFirst() {
        return t1;
    }

    /**
     * sets the first element of the pair to a new value
     *
     * @param t1 - said new value
     */
    protected void setFirst(T1 t1) {
        this.t1 = t1;
    }

    /**
     * get the second element of the pair
     *
     * @return said element
     */
    public T2 getSecond() {
        return t2;
    }

    /**
     * sets the second element of the pair to a new value
     *
     * @param t2 - said new value
     */
    protected void setSecond(T2 t2) {
        this.t2 = t2;
    }

    /**
     * computes the hashcode of the entity based on the elements of the pair
     *
     * @return said hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(t1, t2);
    }

    /**
     * verifies equality with another object
     * it is equal with the object if it is of Pair instance and the attributes are equal
     * (first with first, second with second)
     *
     * @param obj - said object
     * @return ture if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Pair<?, ?> pair))
            return false;
        return Objects.equals(this.t1, pair.t1) && Objects.equals(this.t2, pair.t2);
    }

    @Override
    public String toString() {
        return "{t1=" + t1.toString() +
                ",t2=" + t2.toString() +
                "}";
    }
}
