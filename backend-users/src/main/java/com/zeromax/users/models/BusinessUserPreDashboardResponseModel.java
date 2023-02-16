package com.zeromax.users.models;

import com.zeromax.users.entity.company.CompanyOfficeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessUserPreDashboardResponseModel {

    private String id;
    private String companyName;
    private String companyTin;
    private String email;
    private String mainPhone;
    private CompanyOfficeEntity mainAddress;
    private String pictureUrl;
    private Boolean isActive;
    private Boolean isFinished;
    private Boolean isEmailVerified;
    private Boolean isMainPhoneNumberVerified;
}
