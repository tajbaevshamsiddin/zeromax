package com.zeromax.users.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserProfileInfoModel {

    @NotNull(message = "first name is required")
    private String firstName;
    @NotNull(message = "last name is required")
    private String lastName;
    @NotNull(message = "date of birth is required")
    private Date dateOfBirth;
    private String addressName;
    @NotNull(message = "state is required")
    private String state;
    @NotNull(message = "city is required")
    private String city;
    @NotNull(message = "zip code is required")
    private Integer zipCode;
    @NotNull(message = "address line 1 is required")
    private String addressLine1;
    private String addressLine2;
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
    message = "phone should be similar to this +123456789012")
    private String phone;
}

