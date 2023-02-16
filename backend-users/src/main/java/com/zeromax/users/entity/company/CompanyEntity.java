package com.zeromax.users.entity.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "companies")
public class CompanyEntity {

    @Id
    private String id;
    private String companyName;
    @Indexed(unique = true)
    private String companyTin;
    @Email
    @Indexed(unique = true)
    private String email;
    private String mainPhone;
    private List<PaymentOptions> paymentOptions;
    private String pictureUrl;
    private Double rating = 0.00;
    private Boolean isActive = true;
    private Boolean isEmailVerified = false;
    private Boolean isMainPhoneNumberVerified = false;
    private Boolean isFinished = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}
