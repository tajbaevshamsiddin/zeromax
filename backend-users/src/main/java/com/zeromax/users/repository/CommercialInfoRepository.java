package com.zeromax.users.repository;

import com.zeromax.users.entity.user.CommercialInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommercialInfoRepository extends MongoRepository<CommercialInfo, String> {
}
