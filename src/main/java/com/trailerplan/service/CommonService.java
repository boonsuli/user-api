package com.trailerplan.service;

import com.trailerplan.model.dto.AbstractDTO;
import com.trailerplan.model.entity.AbstractEntity;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * @param <E> Entity
 * @param <D> DTO
 */
public interface CommonService<E extends AbstractEntity, D extends AbstractDTO> {

    List<Optional<D>> findAll() throws Exception;

    List<Optional<D>> findAll(Sort sort) throws Exception;

    Optional<D> findById(Long id) throws Exception;

    E saveOrUpdate(E entity) throws Exception;

    D saveOrUpdate(D dto) throws Exception;

    D deleteById(Long id) throws Exception;

    void deleteByEntity(E entity) throws Exception;

    boolean exists(Long id) throws Exception;
}
