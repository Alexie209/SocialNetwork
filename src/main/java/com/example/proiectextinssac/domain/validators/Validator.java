package com.example.proiectextinssac.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}