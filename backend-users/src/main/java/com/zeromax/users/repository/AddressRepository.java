package com.zeromax.users.repository;

import com.zeromax.users.entity.user.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AddressRepository extends MongoRepository<Address, String> {

    Optional<Address> findByUserIdAndIsMain(String userId, Boolean isMain);

    void deleteAllByUserId(String userId);
}
