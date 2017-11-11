package com.example.jdbcdemo.repository;

import java.util.List;

public interface BaseRepository<T, ID> {

    T save(T entity);

    List<T> findAll();

    T findOne(ID id);

    void delete(ID id);
}
