package com.trailerplan.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trailerplan.model.dto.UserDTO;
import com.trailerplan.model.entity.AbstractEntity_;
import com.trailerplan.model.entity.UserEntity;
import com.trailerplan.model.entity.UserEntity_;
import com.trailerplan.repository.UserRepository;

@Service("userService")
@Transactional
@EnableJpaRepositories("com.trailerplan.repository")
@EntityScan(basePackages = {"com.trailerplan.model.entity"})
@Slf4j
public class UserServiceImpl extends AbstractService<UserEntity, UserDTO> implements UserService {

    @Inject
    public UserServiceImpl(EntityManager entityManager, JpaRepository<UserEntity, Long> repository) {
        super(entityManager, repository, UserEntity.class);
    }

    @Autowired
    public void setRepository(JpaRepository<UserEntity, Long> repository) { this.userRepository = (UserRepository) repository; }
    public JpaRepository<UserEntity, Long> getRepository() { return userRepository; }
    private UserRepository userRepository;

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
