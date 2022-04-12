package ru.javaschool.JavaSchoolBackend2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class AppointmentDto {

    private int id;
    private int pageNumber;
    private String patientName;
    private String treatmentType;
    private String treatmentName;
    private String timePattern;
    private String period;
    private String dose;
    private Date startDate;
    private Date endDate;

}
