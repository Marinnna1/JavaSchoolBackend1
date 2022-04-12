package ru.javaschool.JavaSchoolBackend2.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaschool.JavaSchoolBackend2.dto.MessageDto;
import ru.javaschool.JavaSchoolBackend2.entity.Patient;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PatientsDao {

    private final DoctorDao doctorDao;

    @PersistenceContext
    private EntityManager entityManager;


    public Patient findPatientByName(String name) {
        List<Patient> patients = entityManager
                .createQuery("From Patient where name = \'" + name + "\'", Patient.class).getResultList();
        if(!patients.isEmpty()) {
            return patients.get(0);
        }
        return null;
    }

    public Patient findPatientByEmail(String email) {
        List<Patient> patients = entityManager
                .createQuery("From Patient where email = \'" + email + "\'", Patient.class).getResultList();
        if(!patients.isEmpty()) {
            return patients.get(0);
        }
        return null;
    }

    @Transactional
    public void addPatient(String name, String email, String diagnosis, int insurance, String status, String doctorName) {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setEmail(email);
        patient.setDiagnosis(diagnosis);
        patient.setInsurance(insurance);
        patient.setStatus(status);
        patient.setDoctors(doctorDao.findDoctorByName(doctorName));

        entityManager.persist(patient);
        entityManager.flush();
    }


    @Transactional
    public MessageDto deletePatient(String name) {
        List<Patient> patients = entityManager.createQuery("From Patient where name = \'" + name + "\'", Patient.class).getResultList();
        if(!patients.isEmpty()) {
            entityManager.remove(patients.get(0));
            return new MessageDto("The patient was successfully removed");
        }
        return new MessageDto("Don't find patient");
    }

}
