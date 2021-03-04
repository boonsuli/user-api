package com.trailerplan.repository;

import com.trailerplan.common.DataTest;
import com.trailerplan.common.InterfaceTest;
import com.trailerplan.config.AppUnitTestDataConfigMemory;
import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.UserEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppUnitTestDataConfigMemory.class})
@ActiveProfiles("dev-local-bd-memory-hsql")
public class UserRepositoryIT implements InterfaceTest<UserEntity> {

    @Autowired
    private UserRepository repository;

    private UserEntity entity2save = DataTest.buildUserEntity();
    private UserEntity entitySaved;

    @Before
    public void init() throws Exception {
        entitySaved =  repository.save(entity2save);
    }

    @After
    public void cleanup() throws Exception {
        if(entitySaved!=null && entitySaved.getId()!=null) {
            repository.deleteById(entitySaved.getId());
        }
    }

    @Test
    public void shouldSave() {
        assertNotNull(entitySaved);
        assertNotNull(entitySaved.getId());
        assertTrue(entitySaved.getId().longValue() > 0);
    }

    @Test
    public void shouldUpdate() {
        assertNotNull(entitySaved);
        assertNotNull(entitySaved.getId());
        assertTrue(entitySaved.getId().longValue() > 0);

        entitySaved.setName("newFake");
        UserEntity entityUpdated = repository.save(entitySaved);

        Optional<UserEntity> entityFinded = repository.findById(entityUpdated.getId());
        assertEquals(entityUpdated.getName(), entityFinded.get().getName());
    }

    @Test
    public void shouldDeleteById() {
        assertNotNull(entitySaved);
        assertNotNull(entitySaved.getId());
        assertTrue(entitySaved.getId().longValue() > 0);

        repository.deleteById(entitySaved.getId());
        Optional<UserEntity> entityFinded = repository.findById(entitySaved.getId());
        assertFalse(entityFinded.isPresent());
        entitySaved.setId(null);
    }

    @Test
    public void shouldFindAll() {
        List<UserEntity> entities = repository.findAll();
        assertNotNull(entities);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void shouldFindById() {
        Optional<UserEntity> entityFinded = repository.findById(1L);
        assertTrue(entityFinded.isPresent());
        assertEquals(1L, entityFinded.get().getId().intValue());
    }
}
