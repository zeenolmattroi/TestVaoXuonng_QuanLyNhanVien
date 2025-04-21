package com.example.Backend.Service;


import com.example.Backend.DTO.request.StaffDetailRequest;
import com.example.Backend.DTO.response.StaffDetailResponse;
import com.example.Backend.Entity.*;
import com.example.Backend.Entity.Staff_MajorFacility;
import com.example.Backend.Repository.*;
import com.example.Backend.exception.AppException;
import com.example.Backend.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QlyFacility_Department_MajorService {
    @Autowired
    private FacilitiRepository facilitiRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    Department_FacilityRepositoryRepository departmentFacilityRepositoryRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private Department_FacilityRepository department_facilityRepository;
    @Autowired
    private Major_FacilityRepository majorFacilityRepository;
    @Autowired
    private Staff_MajorFacilityRepository staffMajorFacilityRepository;


    public List<Facility> getListFacility(Integer id){
        return facilitiRepository.findFacilitiesNotAssignedToStaffNative(id);
    }
    public List<Department> getListDepartment(){
        return departmentRepository.findAll();
    }
    public List<Major> getListMajor(){
        return  majorRepository.findAll();
    }

    public List<Department_Facility> getCS_PB(){
        return departmentFacilityRepositoryRepository.findAll();
    }

    public List<StaffDetailResponse> getDetail(Integer id) {
        return staffRepository.findStaffDetail(id);
    }

    public List<Department> getListDepartmentByFac(Integer id) {
       return departmentRepository.findDepartmentsByFacilityId(id);
    }

    public List<Major> getListMajorByFacAnDp(Integer idfac, Integer idDp) {
        return majorRepository.findMajorsByFacilityIdAndDepartmentId(idfac,idDp);
    }






    public String saveDetail(StaffDetailRequest request) {

        try {
            Facility facility = facilitiRepository.findById(request.getIdFacility())
                    .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NotEXISTED));
            Department department = departmentRepository.findById(request.getIdDepartment())
                    .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NotEXISTED));

            Department_Facility departMent_Faccility = department_facilityRepository
                    .findByFacilityAndDepartment(facility,department);

            Major major = majorRepository.findById(request.getIdMajor())
                    .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NotEXISTED));
            System.out.println("huhuh");
            System.out.println(major);
            System.out.println(departMent_Faccility);

            Major_Facility majorFacility = majorFacilityRepository.
                    findByMajorAndDepartmentFacility(major,departMent_Faccility);
            System.out.println(majorFacility); // null ở đây

            Staff staff = staffRepository.findById(request.getIdStaff())
                    .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NotEXISTED));

            Staff_MajorFacility newDetail = new Staff_MajorFacility();
            newDetail.setMajorFacility(majorFacility);
            newDetail.setStaff(staff);
            staffMajorFacilityRepository.save(newDetail);

            System.out.println(newDetail);
            return "đã thêm";

        } catch (Exception e) {
            return "thêm thất bại";
        }

    }

    public String deleteDetail(Integer idStaff, Integer idFacility) {
        try{
            staffMajorFacilityRepository.deleteStaffMajorFacilitiesByStaffIdAndFacilityIdNative(idStaff,idFacility);
            return "xóa thành công";
        }catch (Exception e){
            return "xóa thất bại";
        }
    }



}
