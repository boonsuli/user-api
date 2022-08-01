package com.trailerplan.controller;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.trailerplan.common.ControllerTestFactory;
import com.trailerplan.common.DataTest;
import com.trailerplan.common.InterfaceTest;
import com.trailerplan.config.AppTestConfig;
import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.UserEntity;
import com.trailerplan.service.UserService;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This test class use MockMvc of spring framework testing feature in order to test the controller with the mocking service
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppTestConfig.class})
@ActiveProfiles("dev-local-bd-memory-hsql")
public class UserControllerSpringTestIT implements InterfaceTest<UserEntity> {

    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @InjectMocks
    private UserControllerImpl controller;

    @Inject
    private UserService service;

    private UserEntity entity2save = DataTest.buildUserEntity();
    private UserEntity entitySaved;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        entitySaved = service.saveOrUpdate(entity2save);
    }

    @AfterEach
    public void cleanup() throws Exception {
        if(entity2save!=null && entity2save.getId()!=null) {
            service.deleteById(entity2save.getId());
        }
    }

    @Test
    public void shouldSave() throws Exception {
        ControllerTestFactory factory =  new ControllerTestFactory();
        MockHttpServletRequestBuilder servletRequest = factory.getPostServletRequest(entity2save.extractDTO(), controller.getUriDomain());
        ResultActions resultActions = this.mockMvc.perform(servletRequest);

        StatusResultMatchers status = status();
        ContentResultMatchers content = content();

        resultActions.andExpect(status.isCreated());
        resultActions.andExpect(content.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        resultActions.andExpect(jsonPath("$.id", greaterThan(0)));
        resultActions.andExpect(jsonPath("$.name", equalTo("Anton")));
    }

    @Test
    public void shouldUpdate() throws Exception {
        UserDTO userDTO = entitySaved.extractDTO();
        userDTO.setName("XavierUpdate");

        ControllerTestFactory factory =  new ControllerTestFactory();
        MockHttpServletRequestBuilder updateServletRequest = factory.getPutServletRequest(userDTO, controller.getUriDomain());
        ResultActions resultUpdateActions = this.mockMvc.perform(updateServletRequest);

        StatusResultMatchers status = status();
        ContentResultMatchers updateContent = content();

        resultUpdateActions.andExpect(status.is2xxSuccessful());
        resultUpdateActions.andExpect(updateContent.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        resultUpdateActions.andExpect(jsonPath("$.name", equalTo("XavierUpdate")));
    }

    @Test
    public void shouldDeleteById() throws Exception {

        ControllerTestFactory factory =  new ControllerTestFactory();
        MockHttpServletRequestBuilder servletRequest = factory.getDeleteServletRequest(entitySaved.getId(), controller.getUriDomain());
        ResultActions resultActions = this.mockMvc.perform(servletRequest);

        StatusResultMatchers status = status();
        ContentResultMatchers deleteContent = content();

        resultActions.andExpect(status.isOk());
        entitySaved.setId(null);
    }

    @Test
    public void shouldFindById() throws Exception {
        ControllerTestFactory factory =  new ControllerTestFactory();
        MockHttpServletRequestBuilder servletRequest = factory.getGetServletRequest(1L, controller.getUriDomain());
        ResultActions resultActions = this.mockMvc.perform(servletRequest);

        StatusResultMatchers status = status();
        ContentResultMatchers content = content();

        resultActions.andExpect(status.isOk());
        resultActions.andExpect(content.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        resultActions.andExpect(jsonPath("$.id", equalTo(1)));
        resultActions.andExpect(jsonPath("$.name", equalTo("Kilian")));
    }

    @Test
    public void shouldFindAll() throws Exception {
        ControllerTestFactory factory =  new ControllerTestFactory();
        MockHttpServletRequestBuilder servletRequest = factory.getGetServletRequest(controller.getUriDomain());
        ResultActions resultActions = this.mockMvc.perform(servletRequest);

        StatusResultMatchers status = status();
        ContentResultMatchers getContent = content();

        resultActions.andExpect(status.isOk());
        resultActions.andExpect(getContent.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        resultActions.andExpect(jsonPath("$", is(not(empty()))));
    }
}
