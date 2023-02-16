package com.zeromax.users.maps;

import com.zeromax.users.entity.company.CompanyOfficeEntity;
import com.zeromax.users.entity.company.CompanyEntity;
import com.zeromax.users.models.BusinessUserPreDashboardRequestModel;
import com.zeromax.users.models.BusinessUserPreDashboardResponseModel;
import com.zeromax.users.models.CompanyProfileResponseModel;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.10 (Oracle Corporation)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper{

    @Override
    public CompanyOfficeEntity mapBusinessProfileFirstModelToCompanyOfficeEntity(BusinessUserPreDashboardRequestModel model) {

        var office = new CompanyOfficeEntity();
        office.setName(model.getOfficeName());
        office.setState(model.getState());
        office.setCity(model.getCity());
        office.setZipCode(model.getZipCode());
        office.setAddressLine1(model.getAddressLine1());
        office.setAddressLine2(model.getAddressLine2());
        office.setPhone(model.getPhone());
        return office;
    }

    @Override
    public BusinessUserPreDashboardResponseModel mapCompanyEntityToBusinessProfileResponseModel(CompanyEntity companyEntity,
                                                                                                CompanyOfficeEntity companyOfficeEntity) {
        BusinessUserPreDashboardResponseModel model = new BusinessUserPreDashboardResponseModel();
        model.setId(companyEntity.getId());
        model.setCompanyName(companyEntity.getCompanyName());
        model.setCompanyTin(companyEntity.getCompanyTin());
        model.setEmail(companyEntity.getEmail());
        model.setMainPhone(companyEntity.getMainPhone());
        model.setMainAddress(companyOfficeEntity);
        model.setPictureUrl(companyEntity.getPictureUrl());
        model.setIsActive(companyEntity.getIsActive());
        model.setIsFinished(companyEntity.getIsFinished());
        model.setIsEmailVerified(companyEntity.getIsEmailVerified());
        model.setIsMainPhoneNumberVerified(companyEntity.getIsMainPhoneNumberVerified());
        return model;
    }

    @Override
    public CompanyProfileResponseModel mapCompanyEntityToCompanyProfile(CompanyEntity companyEntity) {
        var company = new CompanyProfileResponseModel();
        company.setId(companyEntity.getId());
        company.setCompanyName(companyEntity.getCompanyName());
        company.setCompanyTin(companyEntity.getCompanyTin());
        company.setEmail(companyEntity.getEmail());
        company.setMainPhone(companyEntity.getMainPhone());
        company.setPaymentOptions(companyEntity.getPaymentOptions());
        company.setPictureUrl(companyEntity.getCompanyName());
        company.setRating(company.getRating());
        company.setIsEmailVerified(companyEntity.getIsEmailVerified());
        company.setIsMainPhoneNumberVerified(companyEntity.getIsMainPhoneNumberVerified());
        company.setIsFinished(companyEntity.getIsFinished());
        company.setCreatedAt(companyEntity.getCreatedAt());
        return company;
    }
}
