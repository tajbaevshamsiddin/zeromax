package com.zeromax.users.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserEntity {

  @Id
  private String id;
  private String companyId;
  @Indexed(unique = true)
  @Email
  private String email;
  private String phone;
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private UserType userType;
  private String password;
  private String pictureUrl;
  private Boolean isEmailVerified = false;
  private Boolean isPhoneVerified = false;
  private Boolean isActive = true;
  private LocalDateTime createdAt = LocalDateTime.now();
}
