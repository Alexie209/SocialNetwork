package com.example.proiectextinssac.repository.memory;

import com.example.proiectextinssac.domain.Entity;
import com.example.proiectextinssac.domain.validators.Validator;
import com.example.proiectextinssac.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository<Long, E extends Entity<Long>> implements Repository<Long, E> {
    private final Validator<E> validator;
    private Map<Long, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(Long id) {
        E entity = entities.get(id);
        if (entity == null) {
            throw new IllegalArgumentException("deleted entity doesn't exist");
        }
        entities.remove(id);
        return null;
    }

    @Override
    public void update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);


    }


    @Override
    public E findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");

        return entities.get(id);
    }

    @Override
    public List<E> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public List<E> getFriends(E user) {
        return null;
    }

    @Override
    public E getByUsername(String s) {
        return null;
    }

}
