package com.zeromax.users.models;

import com.zeromax.users.entity.company.PaymentOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfileResponseModel {

    private String id;
    private String companyName;
    private String companyTin;
    private String email;
    private String mainPhone;
    private List<PaymentOptions> paymentOptions;
    private String pictureUrl;
    private Double rating;
    private Boolean isEmailVerified;
    private Boolean isMainPhoneNumberVerified;
    private Boolean isFinished;
    private LocalDateTime createdAt;
}
