package com.trailerplan.controller;


import com.trailerplan.common.DataTest;
import com.trailerplan.common.InterfaceTest;
import com.trailerplan.config.AppIntegrationTestConfig;
import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.UserEntity;
import com.trailerplan.service.UserService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

/**
 * This test class use spring framework testing feature in order to test the controller w
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@EnableAutoConfiguration
@ContextConfiguration(classes = {AppIntegrationTestConfig.class})
@ActiveProfiles("dev-local-bd-memory-hsql")
public class UserControllerRestAssuredIT implements InterfaceTest<UserEntity> {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Inject
    private UserService service;

    private UserEntity entity2save = DataTest.buildUserEntity();
    private UserEntity entitySaved;

    @Before
    public void init() throws Exception {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        entitySaved = service.saveOrUpdate(entity2save);
    }

    @Test
    public void cleanup() throws Exception {

    }

    @Test
    public void shouldSave() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(this.entity2save.extractDTO().toJson())
            .when()
                .post("/api/user")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .assertThat()
                    .body("id", greaterThan(0))
                    .body("name", equalTo("Anton"));
    }

    @Test
    public void shouldUpdate() throws Exception {
        UserDTO userDTO = entitySaved.extractDTO();
        userDTO.setName("XavierUpdate");

        given()
            .contentType(ContentType.JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(userDTO.toJson())
            .when()
                .put("/api/user")
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .assertThat()
                    .body("name", equalTo("XavierUpdate"));
    }

    @Test
    public void shouldDeleteById() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .delete("/api/user/{id}", entitySaved.getId())
            .then()
                .statusCode(OK.value())
                .assertThat()
                .body("name", equalTo("Anton"));
    }

    @Test
    public void shouldFindById() throws Exception {
        Integer expected = 1;
        given()
            .when()
            .get("/api/user/{id}", expected)
            .then()
            .statusCode(OK.value())
            .body("id", equalTo(expected))
            .body("name", equalTo("Kilian"));
    }

    @Test
    public void shouldFindAll() throws Exception {
        given()
            .when()
                .get("/api/user")
            .then()
                .statusCode(OK.value())
                .body("size()", is(not(empty())));
    }
}
