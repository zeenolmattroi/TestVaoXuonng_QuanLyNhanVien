package com.example.Backend.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDetailRequest {
    Integer idStaff;
    Integer idFacility;
    Integer idDepartment;
    Integer idMajor;
}
