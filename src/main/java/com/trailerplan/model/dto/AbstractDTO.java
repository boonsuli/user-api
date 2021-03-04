package com.trailerplan.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trailerplan.model.entity.AbstractEntity;
import lombok.Data;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.io.IOException;
import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractDTO<E extends AbstractEntity> implements Serializable {

    protected Long id;
    protected String name;
    protected String shortName;

    // Jackson object mapper
    @JsonIgnore
    protected ObjectMapper objectMapper = new ObjectMapper();

    // Doser mapper for dto extraction
    @JsonIgnore
    protected Mapper dtoMapper = new DozerBeanMapper();

    protected AbstractDTO() {}

    protected AbstractDTO(Long id) {
        this.id = id;
    }

    protected AbstractDTO(String name, String shortName) {
        this.shortName = shortName;
        this.name = name;
    }

    protected AbstractDTO(Long id, String name, String shortName) {
        this.shortName = shortName;
        this.name = name;
        this.id = id;
    }

    public String toJson() throws IOException {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(this);
    }

    public abstract E extractEntity() throws Exception;

    @JsonIgnore
    public abstract Class<E> getEntityClass();
}
