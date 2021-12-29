package com.map_toysocialnetworkgui.repository.paging;

/**
 * class that describes a pageable entity
 */
public class PageableImplementation implements Pageable {
    private int pageNumber;
    private int pageSize;

    /**
     * creates a pageable entity with a page number and a page size
     *
     * @param pageNumber - pageable's page number
     * @param pageSize   - pageable's page size
     */
    public PageableImplementation(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }
}
