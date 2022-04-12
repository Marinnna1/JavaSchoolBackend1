package ru.javaschool.JavaSchoolBackend2.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.javaschool.JavaSchoolBackend2.dao.PatientsDao;
import ru.javaschool.JavaSchoolBackend2.dto.PatientDto;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PatientsServiceTest {

    @Mock
    PatientsDao patientsDao;

    @InjectMocks
    PatientsService patientsService;



    @Test
    @DisplayName("Test add patient with null")
    void addPatientNullTest() {
        PatientDto patientDto = new PatientDto();
        patientsService.addPatient(patientDto);
        verify(patientsDao, times(0)).findPatientByName(patientDto.getName());
    }


    @Test
    @DisplayName("Test delete patient")
    void deletePatientTest() {
        PatientDto patientDto = new PatientDto();
        patientsService.deletePatient(patientDto);
        verify(patientsDao, times(1)).deletePatient(patientDto.getName());
    }

}
