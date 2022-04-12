package ru.javaschool.JavaSchoolBackend2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Data
public class EventDto {

    private int id;
    private int pageNumber;
    private String patientName;
    private String filter;
    private String date;
    private String reason;
    private String status;
    private String treatmentName;
    private String treatmentType;
    private String oldStatus;

}

