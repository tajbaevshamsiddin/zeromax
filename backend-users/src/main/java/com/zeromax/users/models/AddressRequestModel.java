package com.zeromax.users.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestModel {

    @NotNull(message = "name is required")
    private String officeName;
    @NotNull(message = "state is required")
    private String state;
    @NotNull(message = "city is required")
    private String city;
    @NotNull(message = "zipCode is required")
    private Integer zipCode;
    @NotNull(message = "address line 1 is required")
    private String addressLine1;
    private String addressLine2;
    @NotNull(message = "phone is required")
    private String phone;
    private Boolean isMain = false;
}
