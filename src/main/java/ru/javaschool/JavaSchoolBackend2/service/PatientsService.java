package ru.javaschool.JavaSchoolBackend2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaschool.JavaSchoolBackend2.dao.DoctorDao;
import ru.javaschool.JavaSchoolBackend2.dao.PatientsDao;
import ru.javaschool.JavaSchoolBackend2.dto.MessageDto;
import ru.javaschool.JavaSchoolBackend2.dto.PatientDto;
import ru.javaschool.JavaSchoolBackend2.email.EmailValidator;


@Service
@RequiredArgsConstructor
public class PatientsService {

    private PatientsDao patientsDao;

    @Autowired
    public void setJdbcUserDAO(PatientsDao patientsDao) {
        this.patientsDao = patientsDao;
    }

    private final DoctorDao doctorDao;


    public MessageDto addPatient(PatientDto patientDto) {
        EmailValidator emailValidator = new EmailValidator();

        if(patientDto.getName() == null) {
            return new MessageDto("Invalid patient name");
        }
        if (patientsDao.findPatientByName(patientDto.getName()) != null) {
            return new MessageDto("Invalid patient");
        }
        if(!emailValidator.validate(patientDto.getEmail()) || patientsDao.findPatientByEmail(patientDto.getEmail()) != null) {
            return new MessageDto("Invalid email");
        }
        if (patientDto.getDiagnosis().trim().equals("")) {
            return new MessageDto("Invalid diagnosis");
        }
        if (!(patientDto.getInsurance() > 0 && patientDto.getInsurance() < 1000)) {
            return new MessageDto("Invalid insurance");
        }
        if (!(patientDto.getStatus().equals("Treating") || patientDto.getStatus().equals("Health"))) {
            return new MessageDto("Invalid patient status");
        }
        if (doctorDao.findDoctorByName(patientDto.getDoctorName()).isEmpty()) {
            return new MessageDto("Invalid doctor name");
        }
        patientsDao.addPatient(patientDto.getName(), patientDto.getEmail(), patientDto.getDiagnosis(),
                patientDto.getInsurance(), patientDto.getStatus(), patientDto.getDoctorName());
        return new MessageDto("Patient successfully added");

    }


    public MessageDto deletePatient(PatientDto patientDto) {
        return patientsDao.deletePatient(patientDto.getName());
    }
}

