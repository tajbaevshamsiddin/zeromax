package com.zeromax.users.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyOfficesTableView {

    private String id;
    private String name;
    private String state;
    private String city;
    private Integer zipCode;
    private Double rating;
    private Boolean isMain;
    private Integer numberOfServices;
}
