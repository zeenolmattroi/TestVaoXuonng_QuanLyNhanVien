package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "major_facility")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Major_Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "id_department_facility",referencedColumnName = "id")
    Department_Facility departmentFacility;

    @ManyToOne
    @JoinColumn(name = "id_major",referencedColumnName = "id")
    Major major;
}
