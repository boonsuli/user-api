package com.trailerplan.service.impl;


import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.AbstractEntity_;
import com.trailerplan.model.entity.UserEntity;
import com.trailerplan.model.entity.UserEntity_;
import com.trailerplan.repository.UserRepository;
import com.trailerplan.service.UserService;
import com.trailerplan.service.common.AbstractService;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("userService")
@Transactional
@EnableJpaRepositories("com.trailerplan.repository")
@EntityScan(basePackages = {"com.trailerplan.model.entity"})
public class UserServiceImpl extends AbstractService<UserEntity, UserDTO> implements UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    public UserServiceImpl(EntityManager entityManager, JpaRepository<UserEntity, Long> repository) {
        super(entityManager, repository, UserEntity.class);
    }

    @Autowired
    private UserRepository userRepository;
    public JpaRepository<UserEntity, Long> getRepository() { return userRepository; }
    public void setRepository(JpaRepository<UserEntity, Long> repository) { this.userRepository = (UserRepository) repository; }

    @Override
    public List<UserDTO> findByLastName(String lastName) {
        super.initSessionFromEntityManager();
        Predicate predicate = criteriaBuilder.and(
            criteriaBuilder.conjunction(),
            criteriaBuilder.like( criteriaBuilder.upper(rootEntity.get(AbstractEntity_.SHORT_NAME)), "%"+lastName.toUpperCase()+"%"));
        return super.findAllByPredicate(predicate);
    }

    @Override
    public List<UserDTO> findByFirstName(String firstName) {
        super.initSessionFromEntityManager();
        Predicate predicate = criteriaBuilder.and(
            criteriaBuilder.conjunction(),
            criteriaBuilder.like(rootEntity.get(UserEntity_.NAME),  "%"+firstName+"%"));
        return super.findAllByPredicate(predicate);
    }

    @Override
    public List<UserDTO> findByUserCountry(String country) {
        super.initSessionFromEntityManager();
        Predicate predicate = criteriaBuilder.and(
            criteriaBuilder.conjunction(),
            criteriaBuilder.like(rootEntity.get(UserEntity_.COUNTRY),  "%"+country+"%"));
        return super.findAllByPredicate(predicate);
    }

    @Override
    public List<UserDTO> findByBirthday(String birthday) throws ParseException {
        super.initSessionFromEntityManager();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateParse = sdf.parse(birthday);
        ParameterExpression<Date> birthdayParameter = super.criteriaBuilder.parameter(Date.class);
        Predicate predicate = super.criteriaBuilder.equal(rootEntity.get(UserEntity_.BIRTHDAY), dateParse);
        return super.findAllByPredicate(predicate);
    }

    @Override
    public Long countByUserCountry(String country) {
        super.initSessionFromEntityManager();
        Predicate predicate = criteriaBuilder.and(
            criteriaBuilder.conjunction(),
            criteriaBuilder.like(rootCount.get(UserEntity_.COUNTRY),  "%"+country+"%"));
        return super.countAllByPredicate(predicate);
    }

}
