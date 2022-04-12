package ru.javaschool.JavaSchoolBackend2.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PatientDto {

    private int id;
    private String name;
    private String email;
    private String diagnosis;
    private int insurance;
    private String status;
    private String doctorName;

}
