package com.trailerplan.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trailerplan.model.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractProductionEntity<D extends AbstractDTO> extends AbstractEntity implements Serializable {

    public AbstractProductionEntity() {}

    public AbstractProductionEntity(int version) {
        this.version = version;
    }

    public AbstractProductionEntity(String name) { super(name, null); }

    public AbstractProductionEntity(String name, String shortName) { super(name, shortName); }

    public AbstractProductionEntity(String name, String shortName, int version) {
        super(name, shortName);
        this.version = version;
    }

    @Version
    @Column(name = "VERSION")
    @Getter @Setter
    protected int version;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATION_DATE")
    @Getter @Setter
    protected Date creationDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "UPDATE_DATE")
    @Getter @Setter
    protected Date modificationDate;

    @PrePersist
    public void prePersistEntity () {
        Date now = new Date();
        creationDate = now;
        modificationDate = now;
    }

    @PreUpdate
    public void preUpdateEntity() {
        modificationDate = new Date();
    }

    @PostUpdate
    public void postUpdateEntity() { super.updateSuccess=true; }

    @PreRemove
    public void preRemoveEntity() {}

    @PostRemove
    public void postRemoveEntity() {}

    @Override
    public String toString() { return ToStringBuilder.reflectionToString(this); }
}
