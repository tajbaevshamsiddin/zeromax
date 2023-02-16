package com.zeromax.users.maps;

import com.zeromax.users.entity.user.Address;
import com.zeromax.users.entity.user.CommercialInfo;
import com.zeromax.users.entity.user.EmployeeInfo;
import com.zeromax.users.entity.user.UserEntity;
import com.zeromax.users.models.*;
import org.springframework.stereotype.Component;
import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.10 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper{

    @Override
    public UserEntity mapUserCreateModelToUserEntity(CreateUserModel user){
        if (user == null){
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setUserType(user.getUserType());
        userEntity.setPassword(user.getPassword());
        return userEntity;
    }

    @Override
    public CreateUserResponseModel mapUserEntityToCreateUserResponseModel(UserEntity userEntity, String refreshToken, String accessToken) {
        if (userEntity == null){
            return null;
        }
        CreateUserResponseModel userResponseModel = new CreateUserResponseModel();
        userResponseModel.setId(userEntity.getId());
        userResponseModel.setEmail(userEntity.getEmail());
        userResponseModel.setUserType(userEntity.getUserType());
        userResponseModel.setIsActive(userEntity.getIsActive());
        userResponseModel.setAccessToken(accessToken);
        userResponseModel.setRefreshToken(refreshToken);
        return userResponseModel;
    }

    @Override
    public EmployeeProfileResponseModel mapUserEntityToEmployeeProfileResponseModel(UserEntity userEntity,
                                                                                    EmployeeInfo employee) {
        EmployeeProfileResponseModel model = new EmployeeProfileResponseModel();
        model.setId(userEntity.getId());
        model.setCompanyId(userEntity.getCompanyId());
        model.setEmail(userEntity.getEmail());
        model.setFirstName(userEntity.getFirstName());
        model.setLastName(userEntity.getLastName());
        model.setDateOfBirth(userEntity.getDateOfBirth());
        model.setPhone(userEntity.getPhone());
        model.setSsn(employee.getSsn());
        model.setOfficeId(employee.getOfficeId());
        model.setCategoryId(employee.getCategoryId());
        model.setAttachedFileUrl(employee.getAttachedFileUrl());
        model.setPictureUrl(userEntity.getPictureUrl());
        model.setRating(employee.getRating());
        model.setStartedDate(employee.getStartedDate());
        model.setIsEmailVerified(userEntity.getIsEmailVerified());
        model.setIsPhoneVerified(userEntity.getIsPhoneVerified());
        model.setIsActive(userEntity.getIsActive());
        return model;
    }

    @Override
    public Address mapSimpleUserProfileInfoModelToAddressEntity(SimpleUserProfileInfoModel model) {
        if(model == null){
            return null;
        }
        var address = new Address();
        address.setState(model.getState());
        address.setCity(model.getCity());
        address.setName(model.getAddressName());
        address.setZipCode(model.getZipCode());
        address.setAddressLine1(model.getAddressLine1());
        address.setAddressLine2(model.getAddressLine2());
        return address;
    }

    @Override
    public SimpleUserProfileResponseModel mapUserEntityToSimpleUserProfileResponseModel(UserEntity userEntity,
                                                                                        Address address) {
        if(userEntity == null){
            return null;
        }
        SimpleUserProfileResponseModel model = new SimpleUserProfileResponseModel();
        model.setFirstName(userEntity.getFirstName());
        model.setLastName(userEntity.getLastName());
        model.setEmail(userEntity.getEmail());
        model.setId(userEntity.getId());
        model.setDateOfBirth(userEntity.getDateOfBirth());
        model.setPictureUrl(userEntity.getPictureUrl());
        model.setState(address.getState());
        model.setCity(address.getCity());
        model.setName(address.getName());
        model.setZipCode(address.getZipCode());
        model.setAddressLine1(address.getAddressLine1());
        model.setAddressLine2(address.getAddressLine2());
        model.setPhone(userEntity.getPhone());
        model.setIsEmailVerified(userEntity.getIsEmailVerified());
        model.setIsActive(userEntity.getIsActive());
        return model;
    }

    @Override
    public CommercialUserProfileResponseModel mapUserEntityToCommercialUserProfileResponse(UserEntity user,
                                                                                           CommercialInfo commercial,
                                                                                           Address address){
        CommercialUserProfileResponseModel model = new CommercialUserProfileResponseModel();
        model.setId(user.getId());
        model.setEmail(user.getEmail());
        model.setCompanyName(commercial.getCompanyName());
        model.setCompanyTin(commercial.getCompanyTin());
        model.setBusinessType(commercial.getBusinessType());
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model.setPosition(commercial.getPosition());
        model.setPictureUrl(user.getPictureUrl());
        model.setMainAddress(address);
        model.setIsActive(user.getIsActive());
        return model;
    }

    @Override
    public EmployeeRegistrationResponseModel mapUserEntityToEmployeeRegistrationResponseModel(UserEntity userEntity,
                                                                                              String accessToken,
                                                                                              String refreshToken,
                                                                                              String phone) {
        EmployeeRegistrationResponseModel model = new EmployeeRegistrationResponseModel();
        model.setFirstName(userEntity.getFirstName());
        model.setLastName(userEntity.getLastName());
        model.setEmail(userEntity.getEmail());
        model.setPhone(phone);
        model.setAccessToken(accessToken);
        model.setRefreshToken(refreshToken);
        return model;
    }

    @Override
    public EmployeeProfileTableView mapUserEntityToEmployeeProfileTableView(UserEntity userEntity) {
        EmployeeProfileTableView profile = new EmployeeProfileTableView();
        profile.setId(userEntity.getId());
        profile.setFirstName(userEntity.getFirstName());
        profile.setLastName(userEntity.getLastName());
        profile.setPictureUrl(userEntity.getPictureUrl());
        profile.setEmail(userEntity.getEmail());
        profile.setPhone(userEntity.getPhone());
        profile.setDateOfBirth(userEntity.getDateOfBirth());
        profile.setIsActive(userEntity.getIsActive());
        return profile;
    }
}
