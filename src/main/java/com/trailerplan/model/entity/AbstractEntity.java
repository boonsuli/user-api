package com.trailerplan.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.trailerplan.model.dto.AbstractDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

@MappedSuperclass
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class AbstractEntity<D extends AbstractDTO> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntity.class);

    public AbstractEntity() {}

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

    @JsonIgnore
    @Transient
    protected boolean updateSuccess;

    @Transient
    @JsonIgnore
    public String getFullName() { return name + " " + shortName; }

    @PrePersist
    public void prePersistEntity () {
        LOGGER.info("Save entity with fullName : " + getFullName() );
    }

    @PostPersist
    public void postPersist() { if(id!=null) updateSuccess=true; }

    @PreUpdate
    public void preUpdateEntity() { LOGGER.info("Update entity with fullName : " + getFullName() ); }

    @PostUpdate
    public void postUpdateEntity() {}

    @PreRemove
    public void preRemoveEntity() { LOGGER.info("Remove entity with fullName : " + getFullName() ); }

    @PostRemove
    public void postRemoveEntity() {}

    public abstract D extractDTO() throws InvocationTargetException, IllegalAccessException;
}
