package com.example.Backend.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddStaff {
    String code;
    String name;
    String accountFE;
    String accountFPT;
}
