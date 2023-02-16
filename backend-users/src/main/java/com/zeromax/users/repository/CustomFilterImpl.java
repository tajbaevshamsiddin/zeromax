package com.zeromax.users.repository;

import com.zeromax.users.entity.company.CompanyOfficeEntity;
import com.zeromax.users.entity.user.UserEntity;
import com.zeromax.users.entity.user.UserType;
import com.zeromax.users.exeptions.CustomGeneralException;
import com.zeromax.users.maps.UserMapper;
import com.zeromax.users.models.CompanyOfficesTableView;
import com.zeromax.users.models.EmployeeProfileTableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CustomFilterImpl implements CustomFilter {

    private final MongoTemplate mongoTemplate;
    private final UserMapper userMapper;

    @Autowired
    public CustomFilterImpl(MongoTemplate mongoTemplate, UserMapper userMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public Page<EmployeeProfileTableView> getFilteredAndOrderedEmployeesTable(Integer page, Integer size, String companyId, UserType userType,
                                                                         String keyword, String sortBy) {
        Sort sort = createSort(sortBy);
        Pageable pageable =
                sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);

        try {
            Query query = new Query();
            List<Criteria> criteria = createCriteria(userType.toString(), keyword, companyId);
            if (!criteria.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(criteria));
            }
            Query newQuery = Query.of(query).with(pageable);
            var listOfEmployees = mongoTemplate.find(newQuery, UserEntity.class, "users");
            var companyEmployees = listOfEmployees.stream()
                    .map(userMapper::mapUserEntityToEmployeeProfileTableView).collect(Collectors.toList());
            return new PageImpl<>(companyEmployees, pageable, companyEmployees.size());
        }catch(Exception ex){
            throw new CustomGeneralException("Could not query users for given filter options", "0000");
        }
    }

    @Override
    public Page<CompanyOfficesTableView> getFilteredAndOrderedOfficesTable(Integer page, Integer size, String companyId,
                                                                       String officeName, String sortBy) {
        Sort sort = createSort(sortBy);
        Pageable pageable =
                sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);

        try {
            Query query = new Query();
            List<Criteria> criteria = createCriteriaForOfficesTable(companyId, officeName);
            if (!criteria.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(criteria));
            }
            Query newQuery = Query.of(query).with(pageable);
            newQuery.fields().include("id", "name", "state", "city", "zipCode", "rating", "isMain", "numberOfServices");
            return PageableExecutionUtils.getPage(
                    mongoTemplate.find(newQuery, CompanyOfficesTableView.class, "companyOffices"),
                    pageable,
                    () -> mongoTemplate.count(query, CompanyOfficeEntity.class)
            );
        }catch (Exception ex){
            throw new CustomGeneralException("Could not query users for given filter options", "0000");
        }
    }

    private List<Criteria> createCriteria(String userType, String keyword, String companyId){
        List<Criteria> criteria = new ArrayList<>();
        if (userType != null && !userType.isBlank()) {
            criteria.add(Criteria.where("userType").regex(userType, "i"));
        }
        if (keyword != null && !keyword.isBlank()) {
            criteria.add(Criteria.where("firsName").regex(keyword, "i"));
            criteria.add(Criteria.where("lastName").regex(keyword, "i"));
        }
        if (companyId != null && !companyId.isBlank()) {
            criteria.add(Criteria.where("companyId").regex(companyId, "i"));
        }
        return criteria;
    }

    private List<Criteria> createCriteriaForOfficesTable(String companyId, String officeName){
        List<Criteria> criteria = new ArrayList<>();
        if (companyId != null && !companyId.isBlank()){
            criteria.add(Criteria.where("companyId").regex(companyId, "i"));
        }
        if (officeName != null && !officeName.isBlank()){
            criteria.add(Criteria.where("name").regex(officeName, "i"));
        }
        return criteria;
    }

    private Sort createSort(String order) {
        Sort sort = null;
        if (order != null) {
            String[] arr = order.split("_");
            if (arr.length == 2) {
                var direction =
                        arr[1].toLowerCase(Locale.ROOT).equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
                sort = Sort.by(new Sort.Order(direction, arr[0]));
            }
        }
        return sort;
    }
}
