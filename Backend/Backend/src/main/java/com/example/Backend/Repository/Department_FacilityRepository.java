package com.example.Backend.Repository;

import com.example.Backend.Entity.Department;
import com.example.Backend.Entity.Department_Facility;
import com.example.Backend.Entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Department_FacilityRepository extends JpaRepository<Department_Facility,Integer> {
    Department_Facility findByFacilityAndDepartment(Facility facility, Department department);

}
