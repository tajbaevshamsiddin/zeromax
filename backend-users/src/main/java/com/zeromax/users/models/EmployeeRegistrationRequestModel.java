package com.zeromax.users.models;

import com.zeromax.users.entity.VerificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRegistrationRequestModel {

    @NotNull(message = "phone is required")
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
            message = "phone should be similar to this +123456789012")
    private String phone;
    @Email
    @NotNull(message = "email is required")
    private String email;
    @NotNull(message = "ssn is required")
    private String ssn;
    @NotNull(message = "office id is required")
    private String officeId;
    @NotNull(message = "category id is required")
    private String categoryId;
    private String attachedFileUrl;
    @NotNull(message = "send by is required")
    private VerificationType sendBy;
}
