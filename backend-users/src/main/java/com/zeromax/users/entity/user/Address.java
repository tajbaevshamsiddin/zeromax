package com.zeromax.users.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "address")
public class Address {

    @Id
    private String id;
    private String userId;
    private String name;
    private String state;
    private String city;
    private Integer zipCode;
    private String addressLine1;
    private String addressLine2;
    private Boolean isMain = false;
}
