package com.zeromax.users.service;

import com.zeromax.users.entity.user.Address;
import com.zeromax.users.entity.VerificationType;
import com.zeromax.users.entity.company.CompanyEntity;
import com.zeromax.users.entity.user.*;
import com.zeromax.users.exeptions.CustomGeneralException;
import com.zeromax.users.exeptions.InvalidRequestException;
import com.zeromax.users.exeptions.NotFoundRequestException;
import com.zeromax.users.exeptions.UnauthorizedRequestException;
import com.zeromax.users.maps.UserMapper;
import com.zeromax.users.models.*;
import com.zeromax.users.repository.*;
import com.zeromax.users.utils.Constants;
import com.zeromax.users.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final CompanyService companyService;
    private final EmailSenderService emailSenderService;
    private final VerificationService verificationService;
    private final PhoneSenderService phoneSenderService;
    private final PasswordResetService passwordResetService;
    private final CommercialInfoRepository commercialInfoRepository;
    private final EmployeeInfoRepository employeeInfoRepository;
    private final AddressRepository addressRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper,
                           RefreshTokenService refreshTokenService, CompanyService companyService,
                           EmailSenderService emailSenderService, VerificationService verificationService,
                           PhoneSenderService phoneSenderService, PasswordResetService passwordResetService,
                           CommercialInfoRepository commercialInfoRepository,
                           EmployeeInfoRepository employeeInfoRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.refreshTokenService = refreshTokenService;
        this.companyService = companyService;
        this.emailSenderService = emailSenderService;
        this.verificationService = verificationService;
        this.phoneSenderService = phoneSenderService;
        this.passwordResetService = passwordResetService;
        this.commercialInfoRepository = commercialInfoRepository;
        this.employeeInfoRepository = employeeInfoRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public PreDashboardModel fillPreDashboard(UserEntity user){
        if (user.getUserType() == UserType.COMPANY){
            var company = companyService.getCompanyById(user.getId());
            var preDashboardInfo = new PreDashboardModel();
            preDashboardInfo.setIsProfileFilled(company.getCompanyTin() != null);
            preDashboardInfo.setIsFinished(company.getIsFinished());
            return preDashboardInfo;
        }
        return null;
    }

    @Override
    public CreateUserResponseModel createUser(CreateUserModel user) throws InvalidRequestException {
        var opt = userRepository.findByEmail(user.getEmail());
        if (opt.isPresent()){
            throw new InvalidRequestException("Email already registered", "0000");
        }
        if (user.getUserType() == UserType.EMPLOYEE){
            throw new InvalidRequestException();
        }
        UserEntity userEntity = userMapper.mapUserCreateModelToUserEntity(user);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setIsActive(true);
        var createdUser = userRepository.save(userEntity);
        if (user.getUserType() == UserType.COMPANY) {
            CompanyEntity companyEntity = new CompanyEntity();
            companyEntity.setId(createdUser.getId());
            companyEntity.setEmail(user.getEmail());
            companyEntity.setIsActive(true);
            var company = companyService.registerCompany(companyEntity);
            userEntity.setCompanyId(company.getId());
            userRepository.save(userEntity);
        }
        if (user.getUserType() == UserType.COMMERCIAL){
            var commercialUser = new CommercialInfo();
            commercialUser.setId(createdUser.getId());
        }
        var userWithId = getUserByEmail(user.getEmail());
        String refreshToken = JWTUtils.createToken(userWithId, Constants.COMPANY_NAME, Constants.REFRESH_KEY, Constants.REFRESH_TTL);
        refreshTokenService.saveToken(userWithId.getId(), refreshToken);
        String accessToken = JWTUtils.createToken(userWithId, Constants.COMPANY_NAME, Constants.ACCESS_KEY, Constants.ACCESS_TTL);
        var mappedUser = userMapper.mapUserEntityToCreateUserResponseModel(userWithId, refreshToken, accessToken);
        var preDashboardInfo = fillPreDashboard(userWithId);
        mappedUser.setPreDashboardInfo(preDashboardInfo);
        return mappedUser;
    }

    @Override
    public void createEmployee(EmployeeRegistrationRequestModel model, String userId) {
        var user = getUserById(userId);
        if(user.getUserType() != UserType.COMPANY){
            throw new InvalidRequestException();
        }
        var opt = userRepository.findByEmail(model.getEmail());
        if (opt.isPresent()){
            throw new InvalidRequestException("Email already registered", "0000");
        }
        var userEntity = new UserEntity();
        userEntity.setEmail(model.getEmail());
        userEntity.setPhone(model.getPhone());
        userEntity.setCompanyId(user.getCompanyId());
        userEntity.setCompanyId(userId);
        userEntity.setUserType(UserType.EMPLOYEE);
        userEntity.setIsActive(false);
        var savedUser = userRepository.save(userEntity);
        var employeeInfo = new EmployeeInfo();
        employeeInfo.setId(savedUser.getId());
        employeeInfo.setSsn(model.getSsn());
        employeeInfo.setOfficeId(model.getOfficeId());
        employeeInfo.setCategoryId(model.getCategoryId());
        employeeInfo.setAttachedFileUrl(model.getAttachedFileUrl());
        employeeInfoRepository.save(employeeInfo);
        var token = verificationService.getVerificationToken(savedUser.getId(), VerificationType.EMAIL);
        var link = String.format("%s/employee/register/%s", Constants.DIRECT_LINK, token);
        if(model.getSendBy() == VerificationType.PHONE){
            phoneSenderService.sendSms(model.getPhone(), link);
        }else {
            emailSenderService.sendMail(model.getEmail(), "Fill profile info", link);
        }
    }

    @Override
    public EmployeeRegistrationResponseModel employeeCompleteRegistration(EmployeeCompleteRegistrationRequestModel employeeModel, String token) {
        var userId = verificationService.verifyToken(token);
        var user = getUserById(userId);
        user.setFirstName(employeeModel.getFirstName());
        user.setLastName(employeeModel.getLastName());
        user.setPassword(passwordEncoder.encode(employeeModel.getPassword()));
        user.setIsActive(true);
        var employee = getEmployeeInfoById(userId);
        employee.setStartedDate(LocalDateTime.now());
        employeeInfoRepository.save(employee);
        var savedUser = userRepository.save(user);
        String refreshToken = JWTUtils.createToken(savedUser, Constants.COMPANY_NAME, Constants.REFRESH_KEY, Constants.REFRESH_TTL);
        refreshTokenService.saveToken(savedUser.getId(), refreshToken);
        String accessToken = JWTUtils.createToken(savedUser, Constants.COMPANY_NAME, Constants.ACCESS_KEY, Constants.ACCESS_TTL);
        return userMapper.mapUserEntityToEmployeeRegistrationResponseModel(savedUser, accessToken, refreshToken, user.getPhone());
    }

    @Override
    public Page<EmployeeProfileTableView> getPaginatedEmployeesWithSorting(Pageable page, String userId, String keyword, String sortBy){
        var user = getUserById(userId);
        return userRepository.getFilteredAndOrderedEmployeesTable(page.getPageNumber(),
                page.getPageSize(), user.getCompanyId(), UserType.EMPLOYEE, keyword, sortBy);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var opt = userRepository.findByEmail(email);
        if (opt.isPresent()) {
            var usr = opt.get();
            return User.builder()
                    .username(usr.getEmail())
                    .password(usr.getPassword())
                    .roles(usr.getUserType().name())
                    .build();
        } else {
            throw new UsernameNotFoundException("Not user with email " + email + " found");
        }
    }

    @Override
    public UserEntity getUserByEmail(String email) throws NotFoundRequestException{
        var user = userRepository.findByEmail(email);
        if (user.isPresent()){
            return user.get();
        }
        throw new NotFoundRequestException("No user with this email", "0000");
    }

    @Override
    public UserEntity getUserById(String userId) throws NotFoundRequestException {
        var user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        }
        throw new NotFoundRequestException("No such user", "0000");
    }

    private Address getMainAddressByUserId(String userId){
        var optAddress = addressRepository.findByUserIdAndIsMain(userId, true);
        if (optAddress.isEmpty()){
            throw new NotFoundRequestException("No such address",  "0000");
        }
        return optAddress.get();
    }

    private CommercialInfo getCommercialInfoById(String userId){
        var optCommercial = commercialInfoRepository.findById(userId);
        if (optCommercial.isEmpty()){
            throw new NotFoundRequestException("No such user",  "0000");
        }
        return optCommercial.get();
    }

    public EmployeeInfo getEmployeeInfoById(String userId){
        var optEmployee = employeeInfoRepository.findById(userId);
        if (optEmployee.isEmpty()){
            throw new NotFoundRequestException("No such user",  "0000");
        }
        return optEmployee.get();
    }

    @Override
    public void logout(String userId){
        refreshTokenService.checkAndDeleteRefreshToken(userId);
    }

    @Override
    public LoginResponseModel refreshToken(String refreshToken) {
        var refreshRecord = refreshTokenService.getToken(refreshToken);
        if (refreshRecord.isEmpty()) {
            throw new UnauthorizedRequestException(
                    "Token is expired or invalid, please login again to take new refresh token", "0401"
            );
        }

        var claims = JWTUtils.getUserDetails(refreshToken, Constants.REFRESH_KEY);

        Optional<UserEntity> optUser = userRepository.findById(claims.getId());
        if (optUser.isPresent()) {
            LoginResponseModel responseModel = new LoginResponseModel();
            String newAccess = JWTUtils.createToken(optUser.get(),
                    Constants.COMPANY_NAME,
                    Constants.ACCESS_KEY,
                    Constants.ACCESS_TTL);
            responseModel.setId(optUser.get().getId());
            responseModel.setEmail(optUser.get().getEmail());
            responseModel.setIsActive(optUser.get().getIsActive());
            responseModel.setUserType(optUser.get().getUserType());
            responseModel.setRefreshToken(refreshToken);
            responseModel.setAccessToken(newAccess);
            var preDashboardInfo = fillPreDashboard(optUser.get());
            responseModel.setPreDashboardInfo(preDashboardInfo);
            return responseModel;
        } else {
            throw new UnauthorizedRequestException();
        }
    }

    @Override
    public void updatePassword(UpdatePasswordRequestModel model, String userId) {
        var user = getUserById(userId);
        if(passwordEncoder.matches(model.getOldPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(model.getPassword()));
            userRepository.save(user);
        }
        else {
            throw new InvalidRequestException("Old password is not matching");
        }
    }

    @Override
    public void sendLinkToResetPassword(String email) {
        var user = getUserByEmail(email);
        var userId = user.getId();
        var link = passwordResetService.getLinkToResetPassword(userId);
        emailSenderService.sendMail(email, "Reset Password Link", link);
    }

    @Override
    public void resetPassword(String token, String password) {
        var userId = verificationService.verifyToken(token);
        var user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
//        var user = getUserById(userId);
//        if (user.getUserType() == UserType.COMPANY)
//            companyService.deleteCompanyById(user.getCompanyId());
//        if (user.getUserType() == UserType.EMPLOYEE)
//            employeeInfoRepository.deleteById(user.getId());
//        if (user.getUserType() == UserType.COMMERCIAL)
//            commercialInfoRepository.deleteById(user.getId());
//        userRepository.deleteById(userId);
//        addressRepository.deleteAllByUserId(userId);
        throw new NotFoundRequestException("you cant delete user or company, Fayoz didnt answer", "0000");
    }

    @Override
    public SimpleUserProfileResponseModel fillSimpleUserProfileInfo(SimpleUserProfileInfoModel simpleUserProfileInfoModel,
                                                                    String userId) {
        var userEntity = getUserById(userId);
        userEntity.setFirstName(simpleUserProfileInfoModel.getFirstName());
        userEntity.setLastName(simpleUserProfileInfoModel.getLastName());
        userEntity.setDateOfBirth(simpleUserProfileInfoModel.getDateOfBirth());
        userEntity.setPhone(simpleUserProfileInfoModel.getPhone());
        var user = userRepository.save(userEntity);
        var mappedAddress = userMapper.mapSimpleUserProfileInfoModelToAddressEntity(simpleUserProfileInfoModel);
        mappedAddress.setUserId(userId);
        mappedAddress.setIsMain(true);
        var savedAddress = addressRepository.save(mappedAddress);
        return userMapper.mapUserEntityToSimpleUserProfileResponseModel(user, savedAddress);

    }

    public SimpleUserProfileResponseModel getSimpleUserProfile(String userId){
        var user = getUserById(userId);
        if (user.getUserType() != UserType.SIMPLE)
            throw new InvalidRequestException();
        var optAddress = addressRepository.findByUserIdAndIsMain(userId, true);
        Address address;
        address = optAddress.orElseGet(Address::new);
        return userMapper.mapUserEntityToSimpleUserProfileResponseModel(user, address);
    }

    @Override
    public CommercialUserProfileResponseModel fillCommercialUserProfileFirstStep(CommercialUserProfileFirstRequestModel model,
                                                                                 String userId) {
        var user = getUserById(userId);
        user.setPhone(model.getPhone());
        userRepository.save(user);
        var commercial = getCommercialInfoById(userId);
        commercial.setCompanyName(model.getCompanyName());
        commercial.setCompanyTin(model.getCompanyTin());
        var savedCommercial = commercialInfoRepository.save(commercial);
        var address = new Address();
        address.setName(model.getAddressName());
        address.setState(model.getState());
        address.setCity(model.getCity());
        address.setUserId(userId);
        address.setZipCode(model.getZipCode());
        address.setAddressLine1(model.getAddressLine1());
        address.setAddressLine2(model.getAddressLine2());
        address.setIsMain(true);
        var savedAddress = addressRepository.save(address);
        return userMapper.mapUserEntityToCommercialUserProfileResponse(user, savedCommercial, savedAddress);
    }

    @Override
    public CommercialUserProfileResponseModel fillCommercialUserProfileSecondStep(CommercialUserProfilesSecondRequestModel model,
                                                                                  String userId) {
        var commercial = getCommercialInfoById(userId);
        var user = getUserById(userId);
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        commercial.setPosition(model.getPosition());
        commercial.setBusinessType(model.getBusinessType());
        userRepository.save(user);
        commercialInfoRepository.save(commercial);
        return userMapper.mapUserEntityToCommercialUserProfileResponse(user, commercial, getMainAddressByUserId(userId));
    }

    @Override
    public EmployeeProfileResponseModel getEmployeeProfile(String userId, String employeeId) {
        //TODO: check design employee profile from app and web
        var employee = getUserById(employeeId);
        if (employee.getUserType() != UserType.EMPLOYEE){
            throw new NotFoundRequestException("No such user", "0000");
        }
        var employeeInfo = getEmployeeInfoById(employeeId);
        if(userId == null){
            return userMapper.mapUserEntityToEmployeeProfileResponseModel(employee, employeeInfo);
        } else if (employee.getCompanyId().equals(companyService.getCompanyById(userId).getId())) {
            return userMapper.mapUserEntityToEmployeeProfileResponseModel(employee, employeeInfo);
        }
        throw new NotFoundRequestException("No such user", "0000");
    }

    @Override
    public EmployeeProfileResponseModel changeEmployeeProfile(EmployeeProfileRequestModel model, String userId){
        var user = getUserById(userId);
        if (user.getUserType() != UserType.EMPLOYEE){
            throw new NotFoundRequestException("No such user", "0000");
        }
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setDateOfBirth(model.getDateOfBirth());
        user.setPhone(model.getPhone());
        var savedUser = userRepository.save(user);
        return userMapper.mapUserEntityToEmployeeProfileResponseModel(savedUser, getEmployeeInfoById(userId));
    }

    @Override
    public void sendVerificationLinkToEmail(String userId) {
        var user = getUserById(userId);
        if (user.getIsEmailVerified()){
            throw new CustomGeneralException("Email already verified", "0000");
        }
        var link = verificationService.getVerificationLink(userId, VerificationType.EMAIL);
        emailSenderService.sendMail(user.getEmail(), "Verify your account", link);
    }

    @Override
    public void sendVerificationNumbersToPhone(String userId) {
        var user = getUserById(userId);
        String phone;
        boolean verified;
        if (user.getUserType() == UserType.COMPANY){
            var company = companyService.getCompanyById(userId);
            phone = company.getMainPhone();
            verified = company.getIsMainPhoneNumberVerified();
        } else {
            phone = user.getPhone();
            verified = user.getIsPhoneVerified();
        }
        if (verified){
            throw new CustomGeneralException("Your main phone number already verified", "0000");
        }
        var numbers = verificationService.getVerificationNumbers(userId, VerificationType.PHONE);
        phoneSenderService.sendSms(phone, numbers);
    }

    @Override
    public void verifyEmailByToken(String token) {
        var userId = verificationService.verifyToken(token);
        var user = getUserById(userId);
        user.setIsEmailVerified(true);
        userRepository.save(user);
        if(user.getUserType() == UserType.COMPANY){
            var company = companyService.getCompanyById(user.getCompanyId());
            company.setIsEmailVerified(true);
            if (company.getCompanyTin() != null)
                company.setIsFinished(true);
            companyService.saveCompany(company);
        }
    }

    @Override
    public void verifyPhoneByNumbers(String numbers) {
        var userId = verificationService.verifyToken(numbers);
        var user = getUserById(userId);
        if(user.getUserType() == UserType.COMPANY){
            var company = companyService.getCompanyById(userId);
            company.setIsMainPhoneNumberVerified(true);
            companyService.saveCompany(company);
        } else {
            user.setIsPhoneVerified(true);
            userRepository.save(user);
        }
    }

    @Override
    public void pauseEmployee(String companyId, String employeeId, PauseEmployeeRequestModel pauseModel) {
        var company = companyService.getCompanyById(companyId);
        var user = getUserById(employeeId);
        if (!user.getUserType().equals(UserType.EMPLOYEE) && !company.getId().equals(user.getCompanyId())){
            throw new NotFoundRequestException("No such employee", "0000");
        }
        var employeeInfo = getEmployeeInfoById(employeeId);
        employeeInfo.setPauseDate(pauseModel.getPauseDate());
        employeeInfoRepository.save(employeeInfo);
    }

    @Override
    public Boolean unlockWhenTimeExpired(String employeeId) {
        var user = getUserById(employeeId);
        if (!user.getUserType().equals(UserType.EMPLOYEE)){
            return true;
        }
        var employeeInfo = getEmployeeInfoById(employeeId);
        if (employeeInfo.getPauseDate() == null)
            return true;
        var remainingDays = employeeInfo.getPauseDate().compareTo(LocalDate.now());
        if (remainingDays >= 0){
            return false;
        }
        employeeInfo.setPauseDate(null);
        employeeInfoRepository.save(employeeInfo);
        return true;
    }

    @Override
    public List<EmployeeNameModel> getOfficeEmployees(String companyId, String officeId) {
        var office = companyService.getCompanyOfficeById(companyId, officeId);
        var employeesInfo = employeeInfoRepository.findAllByOfficeId(office.getId());
        if (employeesInfo == null || employeesInfo.size() == 0){
            throw new NotFoundRequestException("No employees for this office", "0000");
        }
        List<EmployeeNameModel> employees = new ArrayList<>();
        for (var employeeInfo : employeesInfo){
            var employee = getUserById(employeeInfo.getId());
            if (employee.getIsActive()) {
                var employeeNameModel = new EmployeeNameModel();
                employeeNameModel.setId(employee.getId());
                employeeNameModel.setName(employee.getFirstName() + " " + employee.getLastName());
                employees.add(employeeNameModel);
            }
        }
        return employees;
    }
}
