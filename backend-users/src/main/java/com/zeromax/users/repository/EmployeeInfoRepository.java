package com.zeromax.users.repository;

import com.zeromax.users.entity.user.EmployeeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmployeeInfoRepository extends MongoRepository<EmployeeInfo, String> {

    List<EmployeeInfo> findAllByOfficeId(String officeId);
}
