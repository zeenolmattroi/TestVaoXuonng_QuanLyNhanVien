package com.example.Backend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffDetailResponse {

    Integer idFacility;
    String nameFacility;

    Integer idDepartment;
    String nameDepartment;

    Integer idMajor;
    String nameMajor;


}
