package ru.noughtscrosses.db.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ru.noughtscrosses.db.model.Round;
import ru.noughtscrosses.exception.ApiException;
import ru.noughtscrosses.exception.code.DbResponse;

import java.util.Optional;
import java.util.UUID;

public interface IRepository<T> {
    T add(T data) throws ApiException;

    Optional<T> findById(UUID id);
}
