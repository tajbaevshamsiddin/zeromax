package com.zeromax.users.service;

import com.zeromax.users.entity.company.CompanyOfficeEntity;
import com.zeromax.users.entity.company.CompanyEntity;
import com.zeromax.users.exeptions.InvalidRequestException;
import com.zeromax.users.exeptions.NotFoundRequestException;
import com.zeromax.users.maps.CompanyMapper;
import com.zeromax.users.models.*;
import com.zeromax.users.repository.CompanyOfficeRepository;
import com.zeromax.users.repository.CompanyRepository;
import com.zeromax.users.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;
    private final CompanyOfficeRepository companyOfficeRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository, UserRepository userRepository, CompanyMapper companyMapper, CompanyOfficeRepository companyOfficeRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.companyMapper = companyMapper;
        this.companyOfficeRepository = companyOfficeRepository;
    }

    @Override
    public CompanyEntity getCompanyByUserId(String userId){
        var user = userRepository.findById(userId);
        if(user.isPresent()){
            return getCompanyById(user.get().getCompanyId());
        }
        throw new NotFoundRequestException("No such company", "0000");
    }

    private CompanyOfficeEntity getCompanyMainOffice(String companyId){
        var office = companyOfficeRepository.findByCompanyIdAndIsMain(companyId, true);
        if(office.isEmpty()){
            throw new NotFoundRequestException("No main office", "0000");
        }
        return office.get();
    }


    @Override
    public CompanyEntity registerCompany(CompanyEntity companyEntity) {
        var company = companyRepository.findByEmail(companyEntity.getId());
        if (company.isPresent()){
            throw new InvalidRequestException("Email already registered");
        }
        return companyRepository.save(companyEntity);
    }

    @Override
    public void saveCompany(CompanyEntity company) {
        companyRepository.save(company);
    }

    @Override
    public void deleteCompanyById(String companyId) {
        throw new InvalidRequestException("you cant delete user or company, Fayoz didnt answer", "0000");
    }

    @Override
    public CompanyEntity getCompanyById(String companyId) {
        var company = companyRepository.findById(companyId);
        if (company.isEmpty()){
            throw new NotFoundRequestException("No such company", "0000");
        }
        return company.get();
    }

    @Override
    public BusinessUserPreDashboardResponseModel fillBusinessUserProfileFirstStep(BusinessUserPreDashboardRequestModel model,
                                                                                  String companyId) {
        var company = getCompanyById(companyId);
        if (company.getCompanyTin() != null){
            throw new InvalidRequestException();
        }
        company.setCompanyName(model.getCompanyName());
        company.setCompanyTin(model.getCompanyTin());
        company.setMainPhone(model.getPhone());
        if (company.getIsEmailVerified() || company.getIsMainPhoneNumberVerified())
            company.setIsFinished(true);
        var savedCompany = companyRepository.save(company);
        var office = companyMapper.mapBusinessProfileFirstModelToCompanyOfficeEntity(model);
        office.setCompanyId(savedCompany.getId());
        office.setIsMain(true);
        var savedOffice = companyOfficeRepository.save(office);
        return companyMapper.mapCompanyEntityToBusinessProfileResponseModel(savedCompany, savedOffice);
    }

    @Override
    public Page<CompanyOfficesTableView> getCompanyOffices(Pageable page, String companyId, String officeName, String sortBy) {
        //TODO: get number of all services assigned to this office
        return companyOfficeRepository.getFilteredAndOrderedOfficesTable(page.getPageNumber(), page.getPageSize(), companyId, officeName, sortBy);
    }

    @Override
    public CompanyOfficeEntity getCompanyOfficeById(String companyId, String officeId) {
        var company = getCompanyById(companyId);
        var office = companyOfficeRepository.findByIdAndCompanyId(officeId, company.getId());
        if (office.isEmpty()){
            throw new NotFoundRequestException("No such office", "0000");
        }
        return office.get();
    }

    @Override
    public CompanyOfficeEntity addNewOfficeToCompany(String companyId, AddressRequestModel addressRequestModel) {
        var company = getCompanyById(companyId);
        var office = new CompanyOfficeEntity();
        office.setName(addressRequestModel.getOfficeName());
        office.setCompanyId(company.getId());
        office.setState(addressRequestModel.getState());
        office.setCity(addressRequestModel.getCity());
        office.setZipCode(addressRequestModel.getZipCode());
        office.setAddressLine1(addressRequestModel.getAddressLine1());
        office.setAddressLine2(addressRequestModel.getAddressLine2());
        office.setPhone(addressRequestModel.getPhone());
        if (addressRequestModel.getIsMain()){
            var mainOffice = getCompanyMainOffice(company.getId());
            mainOffice.setIsMain(false);
            companyOfficeRepository.save(mainOffice);
        }
        office.setIsMain(addressRequestModel.getIsMain());
        companyOfficeRepository.save(office);
        return office;
    }

    @Override
    public CompanyProfileResponseModel getCompanyProfile(String companyId) {
        var company = getCompanyById(companyId);
        return companyMapper.mapCompanyEntityToCompanyProfile(company);
    }
}
