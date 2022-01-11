package com.map_toysocialnetworkgui.repository.paging;

import java.util.stream.Stream;

/**
 * the interface for a generic page
 *
 * @param <E>
 */
public interface Page<E> {
    /**
     * gets a pageable entity
     *
     * @return said pageable entity
     */
    Pageable getPageable();

    /**
     * gets the next pageable entity
     *
     * @return said pageable entity
     */
    Pageable nextPageable();

    /**
     * gets the previous pageable entity
     *
     * @return said pageable entity
     */
    Pageable previousPageable();

    /**
     * gets the content of a page
     *
     * @return
     */
    Stream<E> getContent();
}
