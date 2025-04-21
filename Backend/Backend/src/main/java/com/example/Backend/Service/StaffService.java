package com.example.Backend.Service;

import com.example.Backend.DTO.request.AddStaff;
import com.example.Backend.Entity.Staff;
import com.example.Backend.Mapper.StaffMapper;
import com.example.Backend.Repository.StaffRepository;
import com.example.Backend.exception.AppException;
import com.example.Backend.exception.ErrorCode;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service

public class StaffService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    StaffMapper staffMapper;

    public List<Staff> getList() {
        return staffRepository.findAll();
    }

    public Staff updateStaff(Staff request) {
        return staffRepository.save(request); // xin looix thầy cô do vội quá em làm cơ bản thôi ạ
    }

    public Staff updateStatus(Integer id){
        Staff staff = staffRepository.findById(id).orElseThrow(()->new RuntimeException("ko tìm thấy nhân viên"));
        if(staff.getStatus()==1){
            staff.setStatus(0);
        }else {
            staff.setStatus(1);
        }
        return staffRepository.save(staff);
    }

    public Staff add(AddStaff request) {
        if (staffRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.CODE_EXISTED);
        }
        if (staffRepository.existsByAccountFE(request.getAccountFE())) {
            throw new AppException(ErrorCode.AccountFE_EXISTED);
        }
        if (staffRepository.existsByAccountFPT(request.getAccountFPT())) {
            throw new AppException(ErrorCode.AccountFPT_EXISTED);
        }
        Staff newStaff = staffMapper.toStaff(request);
        newStaff.setStatus(1);
        return staffRepository.save(newStaff);
    }

    public String delete(Integer id) {

        try {
            staffRepository.deleteById(id);
            return "đã xóa id= "+id;
        }catch (Exception e){
            e.printStackTrace();
            return "xóa thất bại";
        }

    }

    public Staff getMyInfo(Integer id) {
        Staff staff = staffRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NotEXISTED));
        return staff;
    }


    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        List<Staff> staffList = staffRepository.findAll();
        Workbook workbook =  new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Staffs");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Code");
        header.createCell(2).setCellValue("Name");
        header.createCell(3).setCellValue("AccountFE");
        header.createCell(4).setCellValue("AccountFPT");
        header.createCell(5).setCellValue("Status");

        int rowNum =1;

        for (Staff staff:staffList){
             Row row = sheet.createRow(rowNum++);
             row.createCell(0).setCellValue(staff.getId());
             row.createCell(1).setCellValue(staff.getCode());
             row.createCell(2).setCellValue(staff.getName());
             row.createCell(3).setCellValue(staff.getAccountFE());
             row.createCell(4).setCellValue(staff.getAccountFPT());
             row.createCell(5).setCellValue(staff.getStatus());

        }
        final ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public void saveStaffByExcel(InputStream file) throws IOException {
        List<Staff> staffList = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(file);
        Sheet  sheet = workbook.getSheetAt(0);

        sheet.forEach(row ->{
            Staff staff = new Staff();
            if(row.getRowNum()!=0){
                staff.setCode(row.getCell(1).getStringCellValue());
                staff.setName(row.getCell(2).getStringCellValue());
                staff.setAccountFE(row.getCell(3).getStringCellValue());
                staff.setAccountFPT(row.getCell(4).getStringCellValue());
                staff.setStatus(1);
            }
            staffList.add(staff);
        });
        staffRepository.saveAll(staffList);
    }
}
