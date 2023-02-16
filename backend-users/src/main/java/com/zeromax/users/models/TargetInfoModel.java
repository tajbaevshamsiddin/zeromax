package com.zeromax.users.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TargetInfoModel {
    private String namespace;
    private String package_name;
    private String sha256_cert_fingerprints;
}
