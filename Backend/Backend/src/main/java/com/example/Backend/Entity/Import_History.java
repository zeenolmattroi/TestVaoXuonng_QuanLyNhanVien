package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "import_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Import_History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "import_date")
    private LocalDateTime importDate;

    @Column(name = "status")
    private String status;

    @Column(name = "record_count")
    private Integer recordCount;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    // Constructors, Getters, Setters
}