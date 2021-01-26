package com.trailerplan.repository;

import com.trailerplan.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends JpaRepository<UserEntity, Long> {
}
