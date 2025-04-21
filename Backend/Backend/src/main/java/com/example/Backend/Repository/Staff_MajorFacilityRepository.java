package com.example.Backend.Repository;

import com.example.Backend.Entity.Staff_MajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface Staff_MajorFacilityRepository extends JpaRepository<Staff_MajorFacility,Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM staff_major_facility " +
            "WHERE id IN (" +
            "    SELECT smf.id " +
            "    FROM staff s " +
            "    JOIN staff_major_facility smf ON s.id = smf.id_staff " +
            "    JOIN major_facility mf ON smf.id_major_facility = mf.id " +
            "    JOIN major mj ON mf.id_Major = mj.id " +
            "    JOIN department_facility df ON mf.id_department_facility = df.id " +
            "    JOIN facility f ON df.id_facility = f.id " +
            "    JOIN department d ON df.id_department = d.id " +
            "    WHERE s.id = :staffId AND f.id = :facilityId" +
            ")", nativeQuery = true)
    void deleteStaffMajorFacilitiesByStaffIdAndFacilityIdNative(
            @Param("staffId") Integer staffId,
            @Param("facilityId") Integer facilityId
    );
}
