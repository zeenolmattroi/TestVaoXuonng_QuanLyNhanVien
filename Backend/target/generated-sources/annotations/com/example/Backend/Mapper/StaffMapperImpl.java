package com.example.Backend.Mapper;

import com.example.Backend.DTO.request.AddStaff;
import com.example.Backend.Entity.Staff;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T19:20:14+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class StaffMapperImpl implements StaffMapper {

    @Override
    public Staff toStaff(AddStaff request) {
        if ( request == null ) {
            return null;
        }

        Staff.StaffBuilder staff = Staff.builder();

        staff.code( request.getCode() );
        staff.name( request.getName() );
        staff.accountFE( request.getAccountFE() );
        staff.accountFPT( request.getAccountFPT() );

        return staff.build();
    }
}
