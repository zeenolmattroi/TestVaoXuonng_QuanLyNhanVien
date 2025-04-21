package com.example.Backend.Repository;

import com.example.Backend.DTO.response.StaffDetailResponse;
import com.example.Backend.Entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Integer> {
    boolean existsByCode(String code);

    boolean existsByAccountFE(String accountFE);

    boolean existsByAccountFPT(String accountFPT);

    @Query(value = """
        SELECT
            f.id AS idFacility,
            f.name AS nameFacility,
            d.id AS idDepartment,
            d.name AS nameDepartment,
            mj.id AS idMajor,
            mj.name AS nameMajor
        FROM
            staff AS s
        JOIN
            staff_major_facility AS smf ON s.id = smf.id_staff
        JOIN
            major_facility AS mf ON smf.id_major_facility = mf.id
        JOIN
            major AS mj ON mf.id_Major = mj.id
        JOIN
            department_facility AS df ON mf.id_department_facility = df.id
        JOIN
            facility AS f ON df.id_facility = f.id
        JOIN
            department AS d ON df.id_department = d.id
        WHERE
            s.id = :staffId
    """, nativeQuery = true)
    List<StaffDetailResponse> findStaffDetail(@Param("staffId") Integer staffId);
}
