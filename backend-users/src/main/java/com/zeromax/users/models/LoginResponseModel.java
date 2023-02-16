package com.zeromax.users.models;

import com.zeromax.users.entity.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseModel {

    private String id;
    private String email;
    private UserType userType;
    private Boolean isActive;
    private String accessToken;
    private String refreshToken;
    private PreDashboardModel preDashboardInfo;
}
