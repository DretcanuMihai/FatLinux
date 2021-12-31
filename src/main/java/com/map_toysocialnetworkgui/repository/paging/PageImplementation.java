package com.map_toysocialnetworkgui.repository.paging;

import java.util.stream.Stream;

/**
 * class that describes a page entity
 *
 * @param <T> - type of page entity
 */
public class PageImplementation<T> implements Page<T> {
    private Pageable pageable;
    private Stream<T> content;

    /**
     * creates a page with a pageable entity and content
     *
     * @param pageable - page's pageable entity
     * @param content  - content of page
     */
    public PageImplementation(Pageable pageable, Stream<T> content) {
        this.pageable = pageable;
        this.content = content;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public Pageable nextPageable() {
        return new PageableImplementation(this.pageable.getPageNumber() + 1, this.pageable.getPageSize());
    }

    @Override
    public Stream<T> getContent() {
        return this.content;
    }
}
