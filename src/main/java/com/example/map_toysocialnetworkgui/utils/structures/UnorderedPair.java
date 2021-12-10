package com.example.map_toysocialnetworkgui.utils.structures;

/**
 * a class whose instances represent an unordered pair - that is
 * that as along as two pairs contain the same two elements, they will
 * behave the same
 * (that is, unorderedPair(t1,t2)=unorderedPair(t2,t1) will behave the same)
 * @param <T> - the elements type - must be comparable
 */
public class UnorderedPair<T extends Comparable<T>> extends Pair<T,T> {
    /**
     * creates an unordered pair of two elements
     * in an unordered pair (t1,t2)=(t2,t1) in behaviour
     * @param t1 - first element
     * @param t2 - second element
     */
    public UnorderedPair(T t1, T t2) {
        super(t1, t2);

        if(t1!=null && t1.compareTo(t2)>0) {
            super.setFirst(t2);
            super.setSecond(t1);
        }
    }
}
