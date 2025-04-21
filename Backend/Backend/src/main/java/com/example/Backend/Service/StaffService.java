package com.example.Backend.Service;

import com.example.Backend.DTO.request.AddStaff;
import com.example.Backend.Entity.Import_History;
import com.example.Backend.Entity.Staff;
import com.example.Backend.Mapper.StaffMapper;
import com.example.Backend.Repository.Import_HistoryRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service

public class StaffService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    StaffMapper staffMapper;
    @Autowired
    Import_HistoryRepository importHistoryRepository;


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

//    public void saveStaffByExcel(InputStream file) throws IOException {
//        List<Staff> staffList = new ArrayList<>();
//        Workbook workbook = WorkbookFactory.create(file);
//        Sheet  sheet = workbook.getSheetAt(0);
//
//        sheet.forEach(row ->{
//            if(row.getRowNum()!=0){
//                Staff staff = new Staff();
//                staff.setCode(row.getCell(1).getStringCellValue());
//                staff.setName(row.getCell(2).getStringCellValue());
//                staff.setAccountFE(row.getCell(3).getStringCellValue());
//                staff.setAccountFPT(row.getCell(4).getStringCellValue());
//                staff.setStatus(1);
//                staffList.add(staff);
//            }
//        });
//        staffRepository.saveAll(staffList);
//    }
public void saveStaffByExcel(InputStream inputStream, String fileName) throws IOException { // Nhận InputStream và fileName
    LocalDateTime importDate = LocalDateTime.now();
    Import_History importHistory = new Import_History();
    importHistory.setFileName(fileName);
    importHistory.setImportDate(importDate);
    importHistory.setStatus("Thất bại");
    importHistory.setRecordCount(0);
    importHistory.setErrorMessage(null);

    List<Staff> staffList = new ArrayList<>();
    int recordCount = 0;
    try (Workbook workbook = WorkbookFactory.create(inputStream)) {
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                try {
                    Staff staff = new Staff();
                    staff.setCode(row.getCell(1).getStringCellValue().trim());
                    staff.setName(row.getCell(2).getStringCellValue().trim());
                    staff.setAccountFE(row.getCell(3).getStringCellValue().trim());
                    staff.setAccountFPT(row.getCell(4).getStringCellValue().trim());
                    staff.setStatus(1);
                    staffList.add(staff);
                    recordCount++;
                } catch (Exception e) {
                    System.err.println("Lỗi khi đọc dữ liệu từ hàng " + (i + 1) + ": " + e.getMessage());
                    importHistory.setErrorMessage("Lỗi khi đọc dữ liệu từ file Excel.");
                }
            }
        }
        staffRepository.saveAll(staffList);
        importHistory.setStatus("Thành công");
        importHistory.setRecordCount(recordCount);

    } catch (IOException e) {
        importHistory.setErrorMessage("Lỗi khi đọc file Excel: " + e.getMessage());
        throw e;
    } finally {
        importHistoryRepository.save(importHistory);
    }
    }

    public List<Import_History> getHistory() {
        return importHistoryRepository.findAll();
    }
}
