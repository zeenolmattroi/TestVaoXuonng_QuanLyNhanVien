package com.example.Backend.Repository;

import com.example.Backend.Entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacilitiRepository extends JpaRepository<Facility,Integer> {

    @Query(value = "SELECT f.* " +
            "FROM facility f " +
            "WHERE f.id NOT IN (" +
            "    SELECT df.id_facility " +
            "    FROM staff_major_facility smf " +
            "    JOIN major_facility mf ON smf.id_major_facility = mf.id " +
            "    JOIN department_facility df ON mf.id_department_facility = df.id " +
            "    WHERE smf.id_staff = :idStaff" +
            ")", nativeQuery = true)
    List<Facility> findFacilitiesNotAssignedToStaffNative(@Param("idStaff") Integer idStaff);

}
