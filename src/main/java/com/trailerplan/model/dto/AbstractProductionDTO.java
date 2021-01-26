package com.trailerplan.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trailerplan.model.entity.AbstractEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class AbstractProductionDTO<E extends AbstractEntity>
        extends AbstractDTO<E> implements Serializable {

    protected AbstractProductionDTO() {}
    protected AbstractProductionDTO(String name, String shortName) { super(name, shortName); }

    protected int version;

    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    protected Date creationDate;

    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    protected Date modificationDate;
}
