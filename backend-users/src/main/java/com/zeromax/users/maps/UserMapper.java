package com.zeromax.users.maps;

import com.zeromax.users.entity.user.Address;
import com.zeromax.users.entity.user.CommercialInfo;
import com.zeromax.users.entity.user.EmployeeInfo;
import com.zeromax.users.entity.user.UserEntity;
import com.zeromax.users.models.*;
import org.mapstruct.Mapper;

@Mapper()
public interface UserMapper {

    UserEntity mapUserCreateModelToUserEntity(CreateUserModel user);

    CreateUserResponseModel mapUserEntityToCreateUserResponseModel(UserEntity userEntity, String refreshToken, String accessToken);

    EmployeeProfileResponseModel mapUserEntityToEmployeeProfileResponseModel(UserEntity userEntity, EmployeeInfo employee);

    Address mapSimpleUserProfileInfoModelToAddressEntity(SimpleUserProfileInfoModel model);

    SimpleUserProfileResponseModel mapUserEntityToSimpleUserProfileResponseModel(UserEntity userEntity, Address address);

    CommercialUserProfileResponseModel mapUserEntityToCommercialUserProfileResponse(UserEntity user,
                                                                                    CommercialInfo commercial,
                                                                                    Address address);

    EmployeeRegistrationResponseModel mapUserEntityToEmployeeRegistrationResponseModel(UserEntity userEntity,
                                                                                       String accessToken,
                                                                                       String refreshToken,
                                                                                       String phone);

    EmployeeProfileTableView mapUserEntityToEmployeeProfileTableView(UserEntity userEntity);
}
