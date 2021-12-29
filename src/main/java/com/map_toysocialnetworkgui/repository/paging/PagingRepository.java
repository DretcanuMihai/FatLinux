package com.map_toysocialnetworkgui.repository.paging;


import com.map_toysocialnetworkgui.model.entities.Entity;
import com.map_toysocialnetworkgui.repository.skeletons.CrudRepository;

public interface PagingRepository<ID ,
        E extends Entity<ID>>
        extends CrudRepository<ID, E> {

    Page<E> findAll(Pageable pageable);   // Pageable e un fel de paginator
}
