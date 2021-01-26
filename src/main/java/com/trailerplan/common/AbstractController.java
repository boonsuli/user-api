package com.trailerplan.common;

import com.trailerplan.model.dto.AbstractDTO;
import com.trailerplan.model.entity.AbstractEntity;
import com.trailerplan.service.common.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

/**
 * requestMapping : /$DOMAIN$
 * @param <E>
 */
public abstract class AbstractController<E extends AbstractEntity, D extends AbstractDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

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
        LOGGER.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleServerErrors(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}
