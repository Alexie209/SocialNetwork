package com.example.proiectextinssac.repository;

import com.example.proiectextinssac.domain.Entity;

import java.util.List;

public interface Repository<Long, E extends Entity<Long>> {

    E findOne(Long id);

    List<E> findAll();

    E save(E entity);

    E delete(Long id);

    void update(E entity);

    List<E> getFriends(E user);

    E getByUsername(String s);
}
