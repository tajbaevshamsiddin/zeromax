package com.zeromax.users.entity.user;

import com.zeromax.users.entity.LocationModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employeeInfo")
public class EmployeeInfo {

    @Id
    private String id;
    private String ssn;
    private String officeId;
    private String categoryId;
    private Double rating = 0.00;
    private String attachedFileUrl;
    private LocationModel liveLocation;
    private LocalDateTime startedDate;
    private LocalDate pauseDate;
}
