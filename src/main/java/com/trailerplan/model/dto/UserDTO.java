package com.trailerplan.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trailerplan.model.entity.UserEntity;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserDTO extends AbstractDTO<UserEntity> implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{model.dto.user.mail.notBlank.message}")
    @Size(min = 10, max = 50, message = "{model.dto.user.mail.between.message}")
    private String mail;

    @Past(message = "{model.dto.user.birthday.notPast.message}")
    @NotNull(message = "{model.dto.user.birthday.notNull.message}")
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private Date birthday;

    @NotBlank(message = "{model.dto.user.country.notBlank.message}")
    @Size(min=1, max=20, message="{model.dto.user.country.between.message}")
    private String country;

    @Override
    public UserEntity extractEntity() throws Exception { return dtoMapper.map(this, UserEntity.class); }

    @Override
    public Class<UserEntity> getEntityClass() { return UserEntity.class; }
}
