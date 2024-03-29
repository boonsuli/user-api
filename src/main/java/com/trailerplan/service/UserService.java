package com.trailerplan.service;

import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.UserEntity;

import java.text.ParseException;
import java.util.List;

public interface UserService extends CommonService<UserEntity, UserDTO> {

    List<UserDTO> findByLastName(String lastName);
    List<UserDTO> findByFirstName(String firstName);
    List<UserDTO> findByUserCountry(String country);
    List<UserDTO> findByBirthday(String birthday) throws ParseException;
    Long countByUserCountry(String country);

}
