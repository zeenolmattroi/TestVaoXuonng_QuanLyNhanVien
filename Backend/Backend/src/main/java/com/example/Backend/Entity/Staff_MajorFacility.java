package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "staff_major_facility")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Staff_MajorFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "id_MajorFacility",referencedColumnName = "id")
    Major_Facility majorFacility;

    @ManyToOne
    @JoinColumn(name = "id_Staff",referencedColumnName = "id")
    Staff staff;

}
