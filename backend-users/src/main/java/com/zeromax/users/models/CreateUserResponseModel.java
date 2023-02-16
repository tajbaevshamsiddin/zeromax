package com.zeromax.users.models;

import com.zeromax.users.entity.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponseModel {

    @Id
    private String id;
    private String email;
    private UserType userType;
    private Boolean isActive;
    private PreDashboardModel preDashboardInfo;

// Access credentials
    private String accessToken;
    private String refreshToken;
}
