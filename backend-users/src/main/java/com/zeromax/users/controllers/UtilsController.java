package com.zeromax.users.controllers;

import com.zeromax.users.utils.Constants;
import com.zeromax.users.models.*;
import com.zeromax.users.service.UserService;
import org.bson.json.JsonObject;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping()
public class UtilsController {

    private final UserService userService;

    public UtilsController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/verify/email")
    public ResponseEntity<Void> sendVerificationTokenToEmail(@RequestHeader("userId") String userId){
        userService.sendVerificationLinkToEmail(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify/phone")
    public ResponseEntity<Void> sendVerificationNumbersToPhone(@RequestHeader("userId") String userId){
        userService.sendVerificationNumbersToPhone(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify/email/{token}")
    public RedirectView verifyEmail(@PathVariable("token") String token){
        userService.verifyEmailByToken(token);
        var redirectLink = String.format("%s/congratulations", Constants.FRONTEND_LINK);
        return new RedirectView(redirectLink);
    }

    @GetMapping("/verify/phone/{numbers}")
    public RedirectView verifyPhone(@PathVariable("numbers") String numbers){
        userService.verifyPhoneByNumbers(numbers);
        var redirectLink = String.format("%s/congratulations", Constants.FRONTEND_LINK);
        return new RedirectView(redirectLink);
    }

    @PostMapping("/password/reset/{token}")
    public ResponseEntity<Void> passwordReset(@PathVariable String token, @RequestBody ResetPasswordModel resetPasswordModel){
        userService.resetPassword(token, resetPasswordModel.getPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/employee/register/{token}")
    public ResponseEntity<EmployeeRegistrationResponseModel> employeeCompleteRegistration(@PathVariable("token") String token,
                                                                                          @Valid @RequestBody
                                                                           EmployeeCompleteRegistrationRequestModel model){
        var employee = userService.employeeCompleteRegistration(model, token);
        return ResponseEntity.ok().body(employee);
    }

    @GetMapping("/.well-known/assetlinks.json")
    public ResponseEntity<AppInfoResponseModel> getApplicationData(){
        var targetInfo = new TargetInfoModel();
        targetInfo.setNamespace("android_app");
        targetInfo.setPackage_name("com.zeromax.employee_app");
        targetInfo.setSha256_cert_fingerprints("CA:CE:EE:07:03:79:3D:42:3A:6D:97:27:A5:78:D3:DD:2C:26:0F:1D:40:EE:75:81:8C:4B:65:55:AC:8C:D6:25");
        var appInfo = new AppInfoResponseModel();
        appInfo.setRelation("delegate_permission/common.handle_all_urls");
        appInfo.setTarget(targetInfo);
        return ResponseEntity.ok().body(appInfo);
    }
}
