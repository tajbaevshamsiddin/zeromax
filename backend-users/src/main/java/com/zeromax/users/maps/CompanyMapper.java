package com.zeromax.users.maps;

import com.zeromax.users.entity.company.CompanyEntity;
import com.zeromax.users.entity.company.CompanyOfficeEntity;
import com.zeromax.users.models.*;
import org.mapstruct.Mapper;

@Mapper()
public interface CompanyMapper {

    CompanyOfficeEntity mapBusinessProfileFirstModelToCompanyOfficeEntity(BusinessUserPreDashboardRequestModel model);

    BusinessUserPreDashboardResponseModel mapCompanyEntityToBusinessProfileResponseModel(CompanyEntity companyEntity,
                                                                                         CompanyOfficeEntity companyOfficeEntity);

    CompanyProfileResponseModel mapCompanyEntityToCompanyProfile(CompanyEntity companyEntity);
}
