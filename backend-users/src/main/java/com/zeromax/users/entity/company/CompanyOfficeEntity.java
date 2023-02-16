package com.zeromax.users.entity.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "companyOffices")
public class CompanyOfficeEntity {

    @Id
    private String id;
    private String companyId;
    private String name;
    private String state;
    private String city;
    private Integer zipCode;
    private String addressLine1;
    private String addressLine2;
    private String phone;
    private Double rating = 0.00;
    private Boolean isMain = false;
    private Boolean isPhoneVerified = false;
}
