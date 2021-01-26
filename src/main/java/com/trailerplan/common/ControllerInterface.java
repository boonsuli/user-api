package com.trailerplan.common;

import com.trailerplan.model.dto.AbstractDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ControllerInterface<D extends AbstractDTO> {

    /**
     * API to create an entity
     * requestMapping : {host}:{port}/api/{domain}/
     * method http : POST
     *
     * @param dto2save dto to create
     * @return response entity with id and status CREATED(201)
     * @throws Exception
     */
    ResponseEntity<D> create(D dto2save);

    /**
     * API to update an entity
     * requestMapping : {host}:{port}/api/{domain}
     * method http : PUT
     *
     * @param dto2update dto to update
     * @return response entity with status ACCEPTED(201)
     * @throws Exception
     */
    ResponseEntity<D> update(D dto2update);

    /**
     * API to delete an entity
     * requestMapping : {host}:{port}/api/{domain}/{id2delete}
     * method http : DELETE
     *
     * @param id2delete id of entity to delete
     * @return
     * @throws Exception
     */
    ResponseEntity<D> delete(Long id2delete) throws Exception;

    /**
     * API to find an entity
     * requestMapping : {host}:{port}/api/{domain}/{id2find}
     * method http : GET
     *
     * @param id2find id of entity to find
     * @return dto finded with optional
     */
    ResponseEntity<Optional<D>> findById(Long id2find);

    /**
     * API to find all the entity
     * requestMapping : {host}:{port}/api/{doamin}
     * method http : GET
     *
     * @return
     * @throws Exception
     */
    ResponseEntity<List<Optional<D>>> findAll() throws Exception;
}
