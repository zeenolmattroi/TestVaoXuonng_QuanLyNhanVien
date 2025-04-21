package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "department_facility")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department_Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "id_facility",referencedColumnName = "id")
    Facility facility;

    @ManyToOne
    @JoinColumn(name = "id_department",referencedColumnName = "id")
    Department department;

    Integer headOfDepartment;
}
