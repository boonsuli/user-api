package com.trailerplan.service;

import com.trailerplan.common.DataTest;
import com.trailerplan.common.InterfaceTest;
import com.trailerplan.config.AppUnitTestDataConfigMemory;
import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.UserEntity;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppUnitTestDataConfigMemory.class})
@ActiveProfiles("dev-local-bd-memory-hsql")
public class UserServiceIT implements InterfaceTest<UserEntity> {

    @Autowired
    private UserService service;

    @Mock
    private SessionFactory sessionFactory;

    private UserEntity entity2save = DataTest.buildUserEntity();
    private UserEntity entitySaved;

    @Before
    public void init() throws Exception {
        entitySaved =  service.saveOrUpdate(entity2save);
    }

    @After
    public void cleanup() throws Exception {
        if(entitySaved!=null && entitySaved.getId()!=null) {
            service.deleteById(entitySaved.getId());
        }
    }

    @Test
    public void shouldSave() throws Exception {
        assertNotNull(entitySaved);
        assertNotNull(entitySaved.getId());
        assertTrue(entitySaved.getId().longValue() > 0);
    }

    @Test
    public void shouldUpdate() throws Exception {
        assertNotNull(entitySaved);
        assertNotNull(entitySaved.getId());
        assertTrue(entitySaved.getId().longValue() > 0);

        entitySaved.setName("newFake");
        UserEntity entityUpdated = service.saveOrUpdate(entitySaved);

        Optional<UserDTO> dtoFinded = service.findById(entityUpdated.getId());
        assertEquals(entitySaved.getName(), dtoFinded.get().getName());
    }

    @Test
    public void shouldDeleteById() throws Exception {
        assertNotNull(entitySaved);
        assertNotNull(entitySaved.getId());
        assertTrue(entitySaved.getId().longValue() > 0);

        service.deleteById(entitySaved.getId());
        Optional<UserDTO> entityFinded = service.findById(entitySaved.getId());
        assertFalse(entityFinded.isPresent());
        entitySaved.setId(null);
    }

    @Test
    public void shouldFindAll() throws Exception {
        List<Optional<UserDTO>> entities = service.findAll();
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
        assertEquals(4, entities.size());
    }

    @Test
    public void shouldFindById() throws Exception {
        Optional<UserDTO> entityFinded = service.findById(1L);
        assertTrue(entityFinded.isPresent());
        assertEquals(1L, entityFinded.get().getId().intValue());
    }

    @Test
    public void shouldFindByLastName() {
        String expected = "Dahene";
        List<UserDTO> userDTOS =  service.findByLastName(expected);
        assertNotNull(userDTOS);
        assertFalse(userDTOS.isEmpty());
        assertThat(userDTOS.size(), equalTo(1));
        UserDTO userDTO = userDTOS.get(0);
        assertThat(userDTO.getShortName(), containsString(expected));
    }

    @Test
    public void shouldFindByFisrstName() {
        String expected = "Francois";
        List<UserDTO> userDTOS =  service.findByFirstName(expected);
        assertNotNull(userDTOS);
        assertFalse(userDTOS.isEmpty());
        assertThat(userDTOS.size(), equalTo(1));
        UserDTO userDTO = userDTOS.get(0);
        assertThat(userDTO.getName(), containsString(expected));
    }

    @Test
    public void shouldFindByUserCountry() {
        String expected = "France";
        List<UserDTO> userDTOS =  service.findByUserCountry(expected);
        assertNotNull(userDTOS);
        assertFalse(userDTOS.isEmpty());
        assertThat(userDTOS.size(), equalTo(2));
        UserDTO userDTO = userDTOS.get(0);
        assertThat(userDTO.getCountry(), containsString(expected));
    }

    @Test
    public void shouldFindByBirthday() throws ParseException {
        List<UserDTO> userDTOS = service.findByBirthday("1987-10-27");
        assertNotNull(userDTOS);
        assertFalse(userDTOS.isEmpty());
        assertThat(userDTOS.size(), equalTo(1));
        UserDTO userDTO = userDTOS.get(0);
        assertThat(userDTO.getShortName(), anyOf(containsString("Jornet")));
    }

    @Test
    public void shouldCountByUserCountry() {
        Long nbUser = service.countByUserCountry("France");
        assertThat(nbUser, equalTo(2L));
    }
}
