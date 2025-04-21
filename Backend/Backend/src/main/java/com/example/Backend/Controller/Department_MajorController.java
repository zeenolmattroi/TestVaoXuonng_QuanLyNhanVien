package com.example.Backend.Controller;

import com.example.Backend.DTO.request.StaffDetailRequest;
import com.example.Backend.DTO.response.ApiResponse;
import com.example.Backend.DTO.response.StaffDetailResponse;
import com.example.Backend.Entity.Department;
import com.example.Backend.Entity.Department_Facility;
import com.example.Backend.Entity.Facility;
import com.example.Backend.Entity.Major;
import com.example.Backend.Service.QlyFacility_Department_MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;
import java.util.List;

@RestController
@RequestMapping("/quan-ly/department-major")
public class Department_MajorController {
    @Autowired
    private QlyFacility_Department_MajorService service;

    @PostMapping
    public ApiResponse<String> addDetail(@RequestBody StaffDetailRequest request){
        return ApiResponse.<String>builder()
                .result(service.saveDetail(request)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<List<StaffDetailResponse>> getStaffDetil(@PathVariable Integer id){
        return ApiResponse.<List<StaffDetailResponse>>builder()
                .result(service.getDetail(id)).build();
    }




    @GetMapping("/facility/{id}")
    public ApiResponse<List<Facility>> getListFac(@PathVariable Integer id){
        return ApiResponse.<List<Facility>>builder().result(service.getListFacility(id)).build();
    }
    @GetMapping("/department")
    public ApiResponse<List<Department>> getListDepartment(){
        return ApiResponse.<List<Department>>builder().result(service.getListDepartment()).build();
    }


    @GetMapping("/department/{id}")
    public ApiResponse<List<Department>> getListDepartmentByFac(@PathVariable Integer id){
        return ApiResponse.<List<Department>>builder().result(service.getListDepartmentByFac(id)).build();
    }



    @GetMapping("/major")
    public ApiResponse<List<Major>> getListMajor(){
        return ApiResponse.<List<Major>>builder().result(service.getListMajor()).build();
    }
    @GetMapping("/major/{idfac}/{iddp}")
    public ApiResponse<List<Major>> getListMajorByFacAnDp(@PathVariable Integer idfac, @PathVariable Integer iddp){
        return ApiResponse.<List<Major>>builder().result(service.getListMajorByFacAnDp(idfac,iddp)).build();
    }


    @GetMapping("/fc-dp")
    public ApiResponse<List<Department_Facility>> getFC_DP(){
        return ApiResponse.<List<Department_Facility>>builder().result(service.getCS_PB()).build();
    }


    @DeleteMapping("/delete/{idStaff}/{idFacility}")
    public ApiResponse<String> deleteDetail(@PathVariable Integer idStaff, @PathVariable Integer idFacility) {
        return ApiResponse.<String>builder()
                .result(service.deleteDetail(idStaff, idFacility)).build();
    }

}
