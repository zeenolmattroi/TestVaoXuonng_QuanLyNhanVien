package com.example.Backend.Repository;

import com.example.Backend.Entity.Department_Facility;
import com.example.Backend.Entity.Major;
import com.example.Backend.Entity.Major_Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Major_FacilityRepository extends JpaRepository<Major_Facility,Integer> {
    Major_Facility findByMajorAndDepartmentFacility(Major major, Department_Facility departmentFacility);
}
