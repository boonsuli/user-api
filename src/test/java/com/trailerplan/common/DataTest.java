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
        entity.setMail("anton.krupicka@gmail.com");
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
        dto.setMail("anton.krupicka@gmail.com");
        dto.setCountry("USA");
        return dto;
    }
}
