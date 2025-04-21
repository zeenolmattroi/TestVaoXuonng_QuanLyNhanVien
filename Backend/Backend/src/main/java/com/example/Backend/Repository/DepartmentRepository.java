package com.example.Backend.Repository;

import com.example.Backend.DTO.response.StaffDetailResponse;
import com.example.Backend.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    @Query(value = "SELECT d.id,d.code, d.name FROM facility f " +
            "INNER JOIN department_facility df ON f.id = df.id_facility " +
            "INNER JOIN department d ON d.id = df.id_department " +
            "WHERE f.id = :facilityId", nativeQuery = true)
    List<Department> findDepartmentsByFacilityId(@Param("facilityId") Integer facilityId);
}
