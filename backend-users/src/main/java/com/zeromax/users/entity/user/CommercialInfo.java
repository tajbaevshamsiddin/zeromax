package com.zeromax.users.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "commercialInfo")
public class CommercialInfo {

    @Id
    private String id;
    private String companyName;
    private String companyTin;
    private String position;
    private String businessType;
}
