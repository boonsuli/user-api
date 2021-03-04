package com.trailerplan.controller.impl;

import com.trailerplan.controller.UserController;
import com.trailerplan.common.AbstractController;
import com.trailerplan.common.ControllerInterface;
import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.UserEntity;
import com.trailerplan.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(value = "user")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/user")
@Data
public class UserControllerImpl extends AbstractController<UserEntity, UserDTO>
        implements ControllerInterface<UserDTO>, UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserControllerImpl.class);
    private static final String URI_DOMAIN = "user";

    @Autowired
    private UserService service;

    @Inject
    public UserControllerImpl(UserService userService) {
        this.service = userService;
    }

    @Override
    public String getDomain() { return URI_DOMAIN; }

    @ApiOperation(value = "User creation",
            notes = "Multiple status values can be provided with comma seperated strings",
            responseContainer = "List")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
        headers = {"Content-type=app/json", "Accept=app/json"} )
    @ResponseBody
    public ResponseEntity<UserDTO> create (@Valid @RequestBody UserDTO dto2create) {
        try {
            UserDTO dtoCreated = service.saveOrUpdate(dto2create);
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreated);
        } catch(Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
        headers = {"Content-type=app/json", "Accept=app/json"} )
    @ResponseBody
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserDTO dto2update) {
        try {
            UserDTO dtoUpdated = service.saveOrUpdate(dto2update);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(dtoUpdated);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserDTO> delete(@PathVariable("id") Long id2delete) throws Exception {
        try {
            Optional<UserDTO> dto2delete  = service.findById(id2delete);
            if(dto2delete.isPresent()) {
                UserDTO dtoDeleted =  service.deleteById(id2delete);
                return ResponseEntity.ok().body(dtoDeleted);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE )
    @ResponseBody
    public ResponseEntity<Optional<UserDTO>> findById(@PathVariable("id") Long id2find) {
        try {
            return Optional
                        .ofNullable(service.findById(id2find))
                        .map(userDTO -> ResponseEntity.ok().body(userDTO))
                        .orElseGet( () -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Optional<UserDTO>>> findAll() throws Exception {
        List<Optional<UserDTO>> listDto = service.findAll();
        if(listDto!=null && !listDto.isEmpty()) {
            return ResponseEntity.ok(listDto);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
