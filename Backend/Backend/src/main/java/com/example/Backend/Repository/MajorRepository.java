package com.example.Backend.Repository;

import com.example.Backend.Entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major,Integer> {
    @Query(value = "SELECT m.id,m.code, m.name FROM facility f " +
            "INNER JOIN department_facility df ON f.id = df.id_facility " +
            "INNER JOIN department d ON d.id = df.id_department " +
            "INNER JOIN major_facility dm ON d.id = dm.id_department_facility " +
            "INNER JOIN major m ON m.id = dm.id_major " +
            "WHERE f.id = :facilityId AND d.id = :departmentId", nativeQuery = true)
    List<Major> findMajorsByFacilityIdAndDepartmentId(
            @Param("facilityId") Integer facilityId,
            @Param("departmentId") Integer departmentId
    );
}
