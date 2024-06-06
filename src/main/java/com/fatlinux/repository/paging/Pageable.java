package com.fatlinux.repository.paging;

/**
 * the interface for a generic pageable entity
 */
public interface Pageable {
    /**
     * gets the page number
     *
     * @return said page number
     */
    int getPageNumber();

    /**
     * gets the page size
     *
     * @return said page size
     */
    int getPageSize();
}
