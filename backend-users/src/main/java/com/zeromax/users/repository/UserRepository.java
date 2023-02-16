package com.zeromax.users.repository;

import com.zeromax.users.entity.user.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String>, PagingAndSortingRepository<UserEntity, String>,
        CustomFilter {

    Optional<UserEntity> findByEmail(String email);
}