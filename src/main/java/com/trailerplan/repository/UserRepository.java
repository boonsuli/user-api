package com.trailerplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trailerplan.model.entity.UserEntity;

@Repository
public interface  UserRepository extends JpaRepository<UserEntity, Long> {
}
