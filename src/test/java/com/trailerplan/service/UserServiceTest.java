package com.trailerplan.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import org.exparity.hamcrest.date.DateMatchers;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trailerplan.common.DataTest;
import com.trailerplan.common.InterfaceTest;
import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.UserEntity;
import com.trailerplan.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest implements InterfaceTest<UserEntity> {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    protected CriteriaBuilder criteriaBuilderMock;

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Root<UserEntity> rootEntityMock;

    @Mock
    private Root<UserEntity> rootCountMock;

    @Mock
    private UserRepository repositoryMock;

    @Mock
    private CriteriaQuery<UserEntity> criteriaEntityQueryMock;

    @Mock
    private CriteriaQuery<Long> criteriaCountQueryMock;

    @Mock
    private Query<UserEntity> queryEntityMock;

    @Mock
    private Query<Long> queryCountMock;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

//        when(entityManagerMock.unwrap(Session.class)).thenReturn(sessionMock);
//        when(entityManagerMock.getCriteriaBuilder()).thenReturn(criteriaBuilderMock);
//        when(criteriaBuilderMock.createQuery(UserEntity.class)).thenReturn(criteriaEntityQueryMock);
//        when(criteriaEntityQueryMock.from(UserEntity.class)).thenReturn(rootEntityMock);
//        when(criteriaBuilderMock.createQuery(Long.class)).thenReturn(criteriaCountQueryMock);
//        when(criteriaCountQueryMock.from(UserEntity.class)).thenReturn(rootCountMock);

        service = new UserServiceImpl(entityManagerMock, repositoryMock);
    }

    @AfterEach
    public void cleanup() throws Exception {
    }

    @Test
    public void shouldSave() throws Exception {
        Long expected = 1L;
        UserEntity entity = DataTest.buildUserEntity();
        UserDTO userDTOafterSave = entity.extractDTO();
        userDTOafterSave.setId(expected);
        when(repositoryMock.save(entity)).thenReturn(userDTOafterSave.extractEntity());
        service.setRepository(repositoryMock);

        UserDTO dto = service.saveOrUpdate(entity.extractDTO());
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getId());
        assertThat(dto.getId(), equalTo(expected));
    }

    @Test
    public void shouldUpdate() throws Exception {
        shouldSave();
    }

    @Test
    public void shouldDeleteById() throws Exception {
        Long expected = 1L;
        UserEntity entityDeleted = DataTest.buildUserEntity();
        entityDeleted.setId(expected);
        when(repositoryMock.findById(anyLong())).thenReturn(Optional.of(entityDeleted), Optional.of(entityDeleted), Optional.empty());
        doNothing().when(repositoryMock).deleteById(anyLong());
        service.setRepository(repositoryMock);

        Optional<UserDTO> userDtoFinded = service.findById(entityDeleted.getId());
        Assertions.assertNotNull(userDtoFinded);
        Assertions.assertTrue(userDtoFinded.isPresent());
        Assertions.assertEquals(entityDeleted.getId(), userDtoFinded.get().getId());

        UserDTO userDtoDeleted = service.deleteById(expected);
        Assertions.assertNotNull(userDtoDeleted);
        Assertions.assertEquals(userDtoFinded.get().getId(), userDtoDeleted.getId());

        Optional<UserDTO> userDtoNotExist = service.findById(entityDeleted.getId());
        Assertions.assertFalse(userDtoNotExist.isPresent());
    }

    @Test
    public void shouldFindAll() throws Exception {
        UserEntity entity = DataTest.buildUserEntity();
        List<UserEntity> entities = Lists.newArrayList(entity);
        when(repositoryMock.findAll()).thenReturn(entities);

        List<Optional<UserDTO>> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned);
        Assertions.assertFalse(listReturned.isEmpty());
        assertThat(listReturned.size(), equalTo(1));
    }

    @Test
    public void shouldFindById() throws Exception {
        Long expected = 1L;
        UserEntity entity = DataTest.buildUserEntity();
        entity.setId(expected);
        when(repositoryMock.findById(anyLong())).thenReturn(Optional.of(entity));

        Optional<UserDTO> optionalDto = service.findById(expected);
        Assertions.assertTrue(optionalDto.isPresent());
        assertThat(optionalDto.get().getId(), equalTo(expected));

        verify(repositoryMock).findById(anyLong());
    }

    @Test
    public void shouldFindByLastName() {
        String expected = "Dhaene";
        when(sessionMock.createQuery(criteriaEntityQueryMock)).thenReturn(queryEntityMock);

        List<UserEntity> listEntity = new ArrayList<>();
        UserEntity user = new  UserEntity();
        user.setShortName(expected);
        listEntity.add(user);
        when(queryEntityMock.getResultList()).thenReturn(listEntity);

        List<UserDTO> listDto =  service.findByLastName(expected);
        Assertions.assertNotNull(listDto);
        Assertions.assertFalse(listDto.isEmpty());
        assertThat(listDto.size(), equalTo(1));

        UserDTO userDTO = listDto.get(0);
        assertThat(userDTO.getShortName(), anyOf(containsString(expected)));
    }

    @Test
    public void shouldFindByBirthday() throws ParseException {
        String expected = "1985-12-24";
        when(sessionMock.createQuery(criteriaEntityQueryMock)).thenReturn(queryEntityMock);

        UserEntity entity = new  UserEntity();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateBirthdayExpected = sdf.parse(expected);
        entity.setBirthday(dateBirthdayExpected);
        List<UserEntity> listUserExpected = new ArrayList<>();
        listUserExpected.add(entity);
        when(queryEntityMock.getResultList()).thenReturn(listUserExpected);

        List<UserDTO> listDto =  service.findByBirthday(expected);
        Assertions.assertNotNull(listDto);
        Assertions.assertFalse(listDto.isEmpty());
        assertThat(listDto.size(), equalTo(listUserExpected.size()));
        UserDTO dto = listDto.get(0);
        assertThat(dto.getBirthday(), DateMatchers.sameDay(dateBirthdayExpected));
    }

    @Test
    public void shouldCountByUserCountry() {
        Long expected = 1L;
        when(sessionMock.createQuery(criteriaCountQueryMock)).thenReturn(queryCountMock);
        when(queryCountMock.getSingleResult()).thenReturn(expected);
        when(entityManagerMock.getCriteriaBuilder()).thenReturn(criteriaBuilderMock);
        when(entityManagerMock.unwrap(Session.class)).thenReturn(sessionMock);
        when(criteriaBuilderMock.createQuery(UserEntity.class)).thenReturn(criteriaEntityQueryMock);
        when(criteriaBuilderMock.createQuery(Long.class)).thenReturn(criteriaCountQueryMock);
        when(criteriaCountQueryMock.from(UserEntity.class)).thenReturn(rootCountMock);

        Long nbUser = service.countByUserCountry("France");
        assertThat(nbUser, equalTo(expected));

        verify(sessionMock).createQuery(criteriaCountQueryMock);
        verify(queryCountMock).getSingleResult();
        verify(entityManagerMock).getCriteriaBuilder();
        verify(entityManagerMock).unwrap(Session.class);
        verify(criteriaBuilderMock).createQuery(UserEntity.class);
        verify(criteriaBuilderMock).createQuery(Long.class);
        verify(criteriaCountQueryMock).from(UserEntity.class);
    }
}
