package com.trailerplan.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.trailerplan.common.DataTest;
import com.trailerplan.common.InterfaceTest;
import com.trailerplan.config.AppTestDataConfigMemory;
import com.trailerplan.model.entity.UserEntity;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppTestDataConfigMemory.class})
@ActiveProfiles("dev-local-bd-memory-hsql")
public class UserRepositoryIT implements InterfaceTest<UserEntity> {

    @Autowired
    private UserRepository repository;

    private UserEntity entity2save = DataTest.buildUserEntity();
    private UserEntity entitySaved;

    @BeforeEach
    public void init() throws Exception {
        entitySaved =  repository.save(entity2save);
    }

    @AfterEach
    public void cleanup() throws Exception {
        if(entitySaved!=null && entitySaved.getId()!=null) {
            repository.deleteById(entitySaved.getId());
        }
    }

    @Test
    public void shouldSave() {
        Assertions.assertNotNull(entitySaved);
        Assertions.assertNotNull(entitySaved.getId());
        Assertions.assertTrue(entitySaved.getId().longValue() > 0);
    }

    @Test
    public void shouldUpdate() {
        Assertions.assertNotNull(entitySaved);
        Assertions.assertNotNull(entitySaved.getId());
        Assertions.assertTrue(entitySaved.getId().longValue() > 0);

        entitySaved.setName("newFake");
        UserEntity entityUpdated = repository.save(entitySaved);

        Optional<UserEntity> entityFinded = repository.findById(entityUpdated.getId());
        Assertions.assertEquals(entityUpdated.getName(), entityFinded.get().getName());
    }

    @Test
    public void shouldDeleteById() {
        Assertions.assertNotNull(entitySaved);
        Assertions.assertNotNull(entitySaved.getId());
        Assertions.assertTrue(entitySaved.getId().longValue() > 0);

        repository.deleteById(entitySaved.getId());
        Optional<UserEntity> entityFinded = repository.findById(entitySaved.getId());
        Assertions.assertFalse(entityFinded.isPresent());
        entitySaved.setId(null);
    }

    @Test
    public void shouldFindAll() {
        List<UserEntity> entities = repository.findAll();
        Assertions.assertNotNull(entities);
        Assertions.assertFalse(entities.isEmpty());
    }

    @Test
    public void shouldFindById() {
        Optional<UserEntity> entityFinded = repository.findById(1L);
        Assertions.assertTrue(entityFinded.isPresent());
        Assertions.assertEquals(1L, entityFinded.get().getId().intValue());
    }
}
