package com.example.Backend.Mapper;

import com.example.Backend.DTO.request.AddStaff;
import com.example.Backend.Entity.Staff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    Staff toStaff(AddStaff request);

}
