package com.zeromax.users.repository;

import com.zeromax.users.entity.company.CompanyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CompanyRepository extends MongoRepository<CompanyEntity, String>,
        PagingAndSortingRepository<CompanyEntity, String> , CustomFilter {

    Optional<CompanyEntity> findByEmail(String email);
}
