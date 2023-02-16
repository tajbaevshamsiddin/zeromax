package com.zeromax.users.repository;

import com.zeromax.users.entity.company.CompanyOfficeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyOfficeRepository extends MongoRepository<CompanyOfficeEntity, String>, CustomFilter {

    Optional<CompanyOfficeEntity> findByCompanyIdAndIsMain(String companyId, Boolean isMain);

    List<CompanyOfficeEntity> findAllByCompanyId(String companyId);

    Optional<CompanyOfficeEntity> findByIdAndCompanyId(String id, String companyId);
}
