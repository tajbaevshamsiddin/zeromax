package com.zeromax.users.models;

import com.zeromax.users.entity.user.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserProfileResponseModel {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dateOfBirth;
    private String pictureUrl;
    private String name;
    private String state;
    private String city;
    private Integer zipCode;
    private String addressLine1;
    private String addressLine2;
    private String phone;
    private Boolean isEmailVerified;
    private Boolean isActive;
}
