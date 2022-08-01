package com.trailerplan.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.trailerplan.model.dto.AbstractDTO;
import com.trailerplan.model.entity.AbstractEntity;

/**
 * @param <E> entity
 * @param <D> dto
 */
@Component
@Slf4j
@NoArgsConstructor
public abstract class AbstractService<E extends AbstractEntity, D extends AbstractDTO> {

    protected Session session;
    protected CriteriaBuilder criteriaBuilder;
    protected Class<E> entityClass;
    protected EntityManager entityManager;

    protected CriteriaQuery<E> criteriaEntityQuery;
    protected Root<E> rootEntity;

    protected CriteriaQuery<Long> criteriaCountQuery;
    protected Root<E> rootCount;

    public AbstractService(EntityManager entityManager, JpaRepository<E, Long> repository, Class<E> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.setRepository(repository);
    }

    public abstract JpaRepository<E, Long> getRepository();
    public abstract void setRepository(JpaRepository<E, Long> repository);

    @Transactional(readOnly = true)
    public List<Optional<D>> findAll() {
        List<E> entities = getRepository().findAll();
        return getDtosFromEntities(entities);
    }

    @Transactional(readOnly = true)
    public List<Optional<D>> findAll(Sort sort) {
        List<E> entities = getRepository().findAll(sort);
        return getDtosFromEntities(entities);
    }

    @Transactional(readOnly = true)
    public Optional<D> findById(Long id) throws InvocationTargetException, IllegalAccessException {
        Optional<E> entity = getRepository().findById(id);
        if(entity.isPresent()) {
            log.info(getRepository().getClass()+" findById entity id : "+id);
            return Optional.of((D)entity.get().extractDTO());
        } else {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = false)
    public E saveOrUpdate(E entity2save) throws Exception {
        log.info("Saving : "+ entity2save.toString());
        log.debug("repository :"+ getRepository());
        log.debug("entity :"+entity2save.getClass());
        E entitySaved = getRepository().save(entity2save);
        return entitySaved;
    }

    @Transactional(readOnly = false)
    public D saveOrUpdate(D dto2save) throws Exception {
        log.info("Saving : " + dto2save.toString());
        log.debug("repository :"+ getRepository());
        log.debug("dto :" + dto2save.getClass());
        E entity2save = (E)dto2save.extractEntity();
        E entitySaved = getRepository().save(entity2save);
        return (D)entitySaved.extractDTO();
    }

    @Transactional(readOnly = false)
    public D deleteById(Long id) throws InvocationTargetException, IllegalAccessException {
        Optional<E> entity = getRepository().findById(id);
        if(entity.isPresent()) {
            getRepository().deleteById(id);
            log.info(getRepository().getClass()+" deleteById entity id : "+id);
            return (D)entity.get().extractDTO();
        } else {
            return null;
        }
    }

    @Transactional(readOnly = false)
    public void deleteByEntity(E entity2Delete) {
        log.info("Delete : "+ entity2Delete.toString());
        log.debug("repository :"+ getRepository());
        log.debug("entity :"+entity2Delete);
        getRepository().delete(entity2Delete);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) { return getRepository().existsById(id); }

    @Transactional(readOnly = true)
    protected List<D> findAllByPredicate(Predicate... predicates) {
        criteriaEntityQuery.select(rootEntity);
        criteriaEntityQuery.distinct(true);
        criteriaEntityQuery.where(predicates);

        Query<E> queryEntity = session.createQuery(criteriaEntityQuery);
        List<E> userEntities = queryEntity.getResultList();

        return userEntities.stream()
            .map(item -> {
                D dto = null;
                try {
                    dto = (D) item.extractDTO();
                } catch (InvocationTargetException | IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
                return dto;
            })
            .collect(Collectors.toList());
    }

    protected Long countAllByPredicate(Predicate... predicate) {
        criteriaCountQuery.select(this.criteriaBuilder.count((rootCount)));
        criteriaCountQuery.where(predicate);
        Query<Long> queryCount = session.createQuery(criteriaCountQuery);
        return queryCount.getSingleResult();
    }

    protected void initSessionFromEntityManager() {
        this.session = this.entityManager.unwrap(Session.class);
        if(this.session!=null) {
            this.criteriaBuilder = this.entityManager.getCriteriaBuilder();

            this.criteriaEntityQuery = this.criteriaBuilder.createQuery(this.entityClass);
            this.rootEntity = criteriaEntityQuery.from(this.entityClass);
            this.criteriaCountQuery = this.criteriaBuilder.createQuery(Long.class);
            this.rootCount = criteriaCountQuery.from(this.entityClass);
        }
    }

    private List<Optional<D>> getDtosFromEntities(List<E> entities) {
        List<Optional<D>> listDto = null;
        if( entities!=null && !entities.isEmpty() ) {

            listDto = entities.stream().map(entity -> {
                try {
                    return Optional.of((D)entity.extractDTO());
                } catch (IllegalAccessException | InvocationTargetException e1) {
                    log.error(e1.getMessage(), e1);
                    return Optional.ofNullable((D)null);
                }
            }).collect(Collectors.toList());

        } else {
            log.info("entities list is null or empty");
        }
        return listDto;
    }
}
