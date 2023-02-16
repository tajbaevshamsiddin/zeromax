package com.zeromax.users.service;

import com.zeromax.users.entity.company.CompanyOfficeEntity;
import com.zeromax.users.entity.company.CompanyEntity;
import com.zeromax.users.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {

    CompanyEntity getCompanyByUserId(String userId);

    CompanyEntity registerCompany(CompanyEntity company);

    void saveCompany(CompanyEntity company);

    void deleteCompanyById(String companyId);

    CompanyEntity getCompanyById(String companyId);

    BusinessUserPreDashboardResponseModel fillBusinessUserProfileFirstStep(BusinessUserPreDashboardRequestModel model,
                                                                           String userId);
    Page<CompanyOfficesTableView> getCompanyOffices(Pageable page, String companyId, String officeName, String sortBy);

    CompanyOfficeEntity getCompanyOfficeById(String companyId, String officeId);

    CompanyOfficeEntity addNewOfficeToCompany(String companyId, AddressRequestModel addressRequestModel);

    CompanyProfileResponseModel getCompanyProfile(String companyId);
}
