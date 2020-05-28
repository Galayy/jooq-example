package com.itechart.jooq.repository;

import org.jooq.Condition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CrudRepository<T> {

    Integer SUCCESS_CODE = 1;

    T create(T t);

    T update(T t);

    T findById(UUID id);

    Page<T> search(Condition condition, Pageable pageable);

    Boolean deleteById(UUID id);

}
