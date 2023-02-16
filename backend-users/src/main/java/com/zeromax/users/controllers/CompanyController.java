package com.zeromax.users.controllers;

import com.zeromax.users.entity.company.CompanyOfficeEntity;
import com.zeromax.users.models.*;
import com.zeromax.users.service.CompanyService;
import com.zeromax.users.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    private final UserService userService;

    public CompanyController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @PostMapping("employees/register")
    public ResponseEntity<Void> registerEmployee(@Valid @RequestBody EmployeeRegistrationRequestModel model,
                                                                         @RequestHeader("userId") String companyId){
        userService.createEmployee(model, companyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeProfileTableView>> getPaginatedEmployeesWithSorting(@RequestHeader("userId") String companyId,
                                                                                               @RequestParam(required = false) String keyword,
                                                                                               @RequestParam(required = false) String sortBy,
                                                                                               Pageable page){
        return ResponseEntity.ok().body(userService.getPaginatedEmployeesWithSorting(page, companyId, keyword, sortBy));
    }

    @PatchMapping("/employees/{employeeId}/pause")
    public ResponseEntity<Void> pauseEmployee(@RequestHeader("userId") String companyId,
                                              @PathVariable("employeeId") String employeeId,
                                              @RequestBody PauseEmployeeRequestModel pauseEmployeeRequestModel){
        userService.pauseEmployee(companyId, employeeId, pauseEmployeeRequestModel);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("employees/{employeeId}")
    public ResponseEntity<EmployeeProfileResponseModel> getEmployeeProfileByCompany(@RequestHeader("userId") String companyId,
                                                                                    @PathVariable("employeeId") String employeeId){
        var employee = userService.getEmployeeProfile(companyId, employeeId);
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/pre-dashboard")
    public ResponseEntity<BusinessUserPreDashboardResponseModel> fillCompanyProfileFirstStep(@Valid @RequestBody
                                                                                            BusinessUserPreDashboardRequestModel model,
                                                                                             @RequestHeader("userId") String companyId){
        var company = companyService.fillBusinessUserProfileFirstStep(model, companyId);
        return ResponseEntity.ok().body(company);
    }

    @GetMapping("/offices")
    public ResponseEntity<Page<CompanyOfficesTableView>> getCompanyOffices(@RequestHeader("userId") String companyId,
                                                                       @RequestParam(required = false) String officeName,
                                                                       @RequestParam(required = false) String sortBy,
                                                                       Pageable page){
        var offices = companyService.getCompanyOffices(page, companyId, officeName, sortBy);
        return ResponseEntity.ok(offices);
    }

    @GetMapping("/offices/{officeId}")
    public ResponseEntity<CompanyOfficeEntity> getCompanyOfficeById(@RequestHeader("userId") String companyId,
                                                                      @PathVariable("officeId") String officeId){
        var office = companyService.getCompanyOfficeById(companyId, officeId);
        return ResponseEntity.ok(office);
    }

    @PostMapping("/offices")
    public ResponseEntity<CompanyOfficeEntity> addNewOfficeToCompany(@RequestHeader("userId") String companyId,
                                                           @Valid @RequestBody AddressRequestModel addressRequestModel){
        var office = companyService.addNewOfficeToCompany(companyId, addressRequestModel);
        return ResponseEntity.ok(office);
    }

    @GetMapping("/profile")
    public ResponseEntity<CompanyProfileResponseModel> getCompanyProfile(@RequestHeader("userId") String companyId){
        var company = companyService.getCompanyProfile(companyId);
        return ResponseEntity.ok(company);
    }
}
