package com.zeromax.users.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessUserPreDashboardRequestModel {

    @NotNull(message = "company name is required")
    private String companyName;
    @NotNull(message = "company tin is required")
    private String companyTin;
    @NotNull(message = "officeName is required")
    private String officeName;
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
            message = "phone should be similar to this +123456789012")
    private String phone;
    @NotNull(message = "state is required")
    private String state;
    @NotNull(message = "zip code is required")
    private Integer zipCode;
    @NotNull(message = "address line 1 is required")
    private String addressLine1;
    private String addressLine2;
    @NotNull(message = "city is required")
    private String city;
}
