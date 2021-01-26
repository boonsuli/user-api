package com.trailerplan.common;

import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.UserEntity;
import org.joda.time.DateTime;

public class DataTest {

    public static Integer NUMBER_DB_USER = 2;

    public static UserEntity buildUserEntity() {
        UserEntity entity = new UserEntity();
        entity.setName("Anton");
        entity.setShortName("Krupicka");
        entity.setBirthday(new DateTime(1983, 8, 8, 0, 0).toDate());
        entity.setUserName("akrupicka");
        entity.setPassword("akrupicka");
        entity.setMail("anton.krupicka@gmail.com");
        entity.setStreet("Boulder");
        entity.setZipCode("80301");
        entity.setCity("Colorado");
        entity.setCountry("USA");
        return entity;
    }

    public static UserEntity buildUserEntity(long id) {
        UserEntity entity = buildUserEntity();
        entity.setId(id);
        return entity;
    }

    public static UserDTO buildUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setName("Anton");
        dto.setShortName("Krupicka");
        dto.setBirthday(new DateTime(1983, 8, 8, 0, 0).toDate());
        dto.setUserName("akrupicka");
        dto.setPassword("akrupicka");
        dto.setMail("anton.krupicka@gmail.com");
        dto.setStreet("Boulder");
        dto.setZipCode("80301");
        dto.setCity("Colorado");
        dto.setCountry("USA");
        return dto;
    }
}
