package com.trailerplan.model.entity;


import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.trailerplan.model.dto.AbstractDTO;

@MappedSuperclass
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@Data
@Slf4j
@NoArgsConstructor
public abstract class AbstractEntity<D extends AbstractDTO> implements Serializable {

    public AbstractEntity(String name, String shortName) {
        this.name=name;
        this.shortName=shortName;
    }

    /** Doser mapper for the DTO extractor */
    @JsonIgnore
    @Transient
    protected Mapper entityMapper = new DozerBeanMapper();

    @Id
    @Column(name = "ID", unique=true, nullable=false, insertable=false, updatable=false)
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty("id")
    protected Long id;

    @Column(name = "NAME")
    @JsonProperty("name")
    protected String name;

    @Column(name = "SHORT_NAME", nullable=true)
    @JsonProperty("shortName")
    protected String shortName;

    public abstract D extractDTO() throws InvocationTargetException, IllegalAccessException;
}
