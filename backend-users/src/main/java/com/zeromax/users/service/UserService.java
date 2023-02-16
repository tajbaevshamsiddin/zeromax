package com.zeromax.users.service;

import com.zeromax.users.entity.user.UserEntity;
import com.zeromax.users.exeptions.NotFoundRequestException;
import com.zeromax.users.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface UserService extends UserDetailsService {

    PreDashboardModel fillPreDashboard(UserEntity user);

    CreateUserResponseModel createUser(CreateUserModel createUserModel);

    void createEmployee(EmployeeRegistrationRequestModel model, String userId);

    EmployeeRegistrationResponseModel employeeCompleteRegistration(EmployeeCompleteRegistrationRequestModel employee, String token);

    Page<EmployeeProfileTableView> getPaginatedEmployeesWithSorting(Pageable page, String userId, String name, String sortBy);

    UserEntity getUserByEmail(String email) throws NotFoundRequestException;

    UserEntity getUserById(String user_id) throws NotFoundRequestException;

    void logout(String user_id);

    LoginResponseModel refreshToken(String refresh_token);

    void updatePassword(UpdatePasswordRequestModel model, String user_id);

    void sendLinkToResetPassword(String email);

    void resetPassword(String token, String password);

    void deleteUser(String userId);

    SimpleUserProfileResponseModel fillSimpleUserProfileInfo(SimpleUserProfileInfoModel simpleUserProfileInfoModel,
                                                             String user_id);

    SimpleUserProfileResponseModel getSimpleUserProfile(String userId);

    CommercialUserProfileResponseModel fillCommercialUserProfileFirstStep(CommercialUserProfileFirstRequestModel model,
                                                                          String user_id);

    CommercialUserProfileResponseModel fillCommercialUserProfileSecondStep(CommercialUserProfilesSecondRequestModel model,
                                                                           String user_id);

    EmployeeProfileResponseModel getEmployeeProfile(String userId, String employeeId);

    EmployeeProfileResponseModel changeEmployeeProfile(EmployeeProfileRequestModel model, String userId);

    void sendVerificationLinkToEmail(String userId);

    void sendVerificationNumbersToPhone(String userId);

    void verifyEmailByToken(String token);

    void verifyPhoneByNumbers(String token);

    void pauseEmployee(String companyId, String employeeId, PauseEmployeeRequestModel pauseModel);

    Boolean unlockWhenTimeExpired(String employeeId);

    List<EmployeeNameModel> getOfficeEmployees(String companyId, String officeId);
}
