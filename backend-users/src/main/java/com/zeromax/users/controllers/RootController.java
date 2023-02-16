package com.zeromax.users.controllers;

import com.zeromax.users.models.EmployeeNameModel;
import com.zeromax.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/root")
public class RootController {

    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{officeId}/employees")
    public ResponseEntity<List<EmployeeNameModel>> getOfficeEmployees(@RequestHeader("userId") String companyId,
                                                                      @PathVariable String officeId){
        var employees = userService.getOfficeEmployees(companyId, officeId);
        return ResponseEntity.ok().body(employees);
    }
}
