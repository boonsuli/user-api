package com.trailerplan.controller;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.trailerplan.model.dto.AbstractDTO;
import com.trailerplan.model.entity.AbstractEntity;

/**
 * requestMapping : /$DOMAIN$
 * @param <E>
 */
@Slf4j
public abstract class AbstractController<E extends AbstractEntity, D extends AbstractDTO> {

    protected static final String URI_REST_ROOT = "/api";

    /**
     * Retourne le domaine
     * @return
     */
    public abstract String getDomain();
    public String getUriDomain() { return URI_REST_ROOT + "/" + getDomain(); }

    /**
     * requestMapping : /domain
     * method : POST
     * @param dto2create
     * @return
     * @throws Exception
     */
    public abstract ResponseEntity<D> create(D dto2create) throws Exception;

    /**
     * requestMapping : /domain
     * method : PUT
     * @param dto2update
     * @throws Exception
     */
    public abstract ResponseEntity<D> update(D dto2update) throws Exception;

    /**
     * requestMapping : /
     * method : DELETE
     * @return
     * @throws Exception
     */
    public abstract ResponseEntity<D> delete(Long id2delete) throws Exception;


    /**
     * requestMapping : /findAll
     * method : GET
     * @return
     * @throws Exception
     */
    public abstract ResponseEntity<List<Optional<D>>> findAll() throws Exception;


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleClientErrors(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleServerErrors(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}
