package com.trailerplan.model.entity;

import com.trailerplan.model.dto.UserDTO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@Entity
@Table(name = "P_USER")
@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "FIRST_NAME")),
        @AttributeOverride(name = "shortName", column = @Column(name = "LAST_NAME"))
})
@Data
public class UserEntity extends AbstractProductionEntity<UserDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    public UserEntity() { super(); }

    @Column(name = "BIRTHDAY")
    private Date birthday;

    @Column(name = "AUTH_USERNAME")
    private String userName;

    @Column(name = "AUTH_PASSWORD")
    private String password;

    @Column(name = "AUTH_MAIL")
    private String mail;

    @Column(name = "AUTH_PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ADR_STREET")
    private String street;

    @Column(name = "ADR_ZIPCODE")
    private String zipCode;

    @Column(name = "ADR_CITY")
    private String city;

    @Column(name = "ADR_COUNTRY")
    private String country;

    @Override
    public UserDTO extractDTO() throws InvocationTargetException, IllegalAccessException {
        return entityMapper.map(this, UserDTO.class);
    }
}
