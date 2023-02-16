package com.zeromax.users.repository;

import com.zeromax.users.entity.company.CompanyOfficeEntity;
import com.zeromax.users.entity.user.UserType;
import com.zeromax.users.models.CompanyOfficesTableView;
import com.zeromax.users.models.EmployeeProfileTableView;
import org.springframework.data.domain.Page;

public interface CustomFilter {

    Page<EmployeeProfileTableView> getFilteredAndOrderedEmployeesTable(Integer page, Integer size, String companyId,
                                                                       UserType userType, String keyword, String sortBy);

    Page<CompanyOfficesTableView> getFilteredAndOrderedOfficesTable(Integer page, Integer size, String companyId,
                                                                    String officeName, String sortBy);
}
