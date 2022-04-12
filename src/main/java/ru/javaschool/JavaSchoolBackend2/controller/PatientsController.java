package ru.javaschool.JavaSchoolBackend2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.javaschool.JavaSchoolBackend2.dto.MessageDto;
import ru.javaschool.JavaSchoolBackend2.dto.PatientDto;
import ru.javaschool.JavaSchoolBackend2.service.PatientsService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("patients/")
@RequiredArgsConstructor
public class PatientsController {

    private final PatientsService patientsService;


    @PostMapping("add")
    public MessageDto addPatient(@RequestBody PatientDto patientDto) {
        return patientsService.addPatient(patientDto);
    }


    @PostMapping("delete")
    public MessageDto deletePatient(@RequestBody PatientDto patientDto) {
        return patientsService.deletePatient(patientDto);
    }

}