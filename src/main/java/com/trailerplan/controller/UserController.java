package com.trailerplan.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.trailerplan.model.dto.UserDTO;

public interface UserController {

    ResponseEntity<List<UserDTO>> findByLastName(final String lastName);
    ResponseEntity<List<UserDTO>> findByFirstName(final String firstName);
    ResponseEntity<List<UserDTO>> findByUserCountry(final String country);
    ResponseEntity<List<UserDTO>> findByBirthday(final String birthday) throws ParseException;
    ResponseEntity<Long> countByUserCountry(final String country);

}
