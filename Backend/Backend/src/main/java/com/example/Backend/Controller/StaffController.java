package com.example.Backend.Controller;

import com.example.Backend.DTO.request.AddStaff;
import com.example.Backend.DTO.response.ApiResponse;
import com.example.Backend.Entity.Staff;
import com.example.Backend.Service.StaffService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;


    @GetMapping
    public ApiResponse<List<Staff>> getList(){
        return ApiResponse.<List<Staff>>builder()
                .result(staffService.getList())
                .build();
    }

    @GetMapping("/export-data-to-excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition","attachment; filename=Staffs.xlsx");
        staffService.exportDataToExcel(response);
    }
    @PostMapping("/import-data-to-excel")
    public ApiResponse<String> importExcel(@RequestParam("file")MultipartFile file) throws IOException {
        staffService.saveStaffByExcel(file.getInputStream());
        return ApiResponse.<String>builder()
                .result("đã thêm").build();

    }


    @PostMapping()
    public ApiResponse<Staff> addStaff(@RequestBody AddStaff request){
        return ApiResponse.<Staff>builder()
                .result(staffService.add(request)).build();
    }

    @PutMapping
    public ApiResponse<Staff> updateStaff(@RequestBody Staff staff){

        return ApiResponse.<Staff>builder()
                .result(staffService.updateStaff(staff)).build();
    }

    @PostMapping("/update-status/{id}")
    public ApiResponse<Staff> updateStatus(@PathVariable Integer id){
        return ApiResponse.<Staff>builder()
                .result(staffService.updateStatus(id)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable Integer id){
        return ApiResponse.builder().result(staffService.delete(id)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Staff> getMyInfo(@PathVariable Integer id){
        return ApiResponse.<Staff>builder().result(staffService.getMyInfo(id)).build();
    }



}
