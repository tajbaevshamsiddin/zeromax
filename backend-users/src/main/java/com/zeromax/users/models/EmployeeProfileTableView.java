package com.zeromax.users.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfileTableView {

    private String id;
    private String firstName;
    private String lastName;
    private String pictureUrl;
    private String email;
    private Date dateOfBirth;
    private String phone;
    private Boolean isActive;
}
