package com.trailerplan.model.dto;

import com.trailerplan.model.entity.AbstractEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class ControllerRequestDTO extends AbstractDTO {

    private String mail;
    private String motif;
    private Integer type;
    private Boolean verified;
    private Date startDate;
    private Date endDate;

    public ControllerRequestDTO() {}
    public ControllerRequestDTO(Long id) { super(id); }
    public ControllerRequestDTO(String name) {
        super(name, null);
    }
    public ControllerRequestDTO(String name, String shortName) { super(name, shortName); }
    public ControllerRequestDTO(Long id, String name, String shortName) { super(id, name, shortName); }

    public ControllerRequestDTO setPeriod(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    @Override
    public AbstractEntity extractEntity() throws Exception {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return null;
    }
}
