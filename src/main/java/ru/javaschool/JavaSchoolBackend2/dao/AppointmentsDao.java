package ru.javaschool.JavaSchoolBackend2.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaschool.JavaSchoolBackend2.entity.Appointment;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;


@Transactional
@Repository
@RequiredArgsConstructor
public class AppointmentsDao {

    private final PatientsDao patientsDao;

    private final TreatmentsDao treatmentsDao;

    @PersistenceContext
    private EntityManager entityManager;


    public List<Appointment> findAll(Integer pageNumber) {
        int pageSize = 3;
        int currentPage = pageNumber;
        Query query = entityManager.createQuery("From Appointment");
            query.setFirstResult(((currentPage - 1) * pageSize));
            query.setMaxResults(pageSize);
        return query.getResultList();

    }

    public Long getAppointmentsCount() {
        return (Long) entityManager.createQuery("SELECT COUNT(id) FROM Appointment").getSingleResult();

    }


    public Appointment addAppointment(String patientName, Double dose, Date startDate, Date endDate, String timePattern, String treatmentName) {
        Appointment appointment = new Appointment();

        appointment.setPatient(patientsDao.findPatientByName(patientName));
        appointment.setTreatment(treatmentsDao.findTreatmentByName(treatmentName));
        appointment.setStartDate(startDate);
        appointment.setEndDate(endDate);
        appointment.setTimePattern(timePattern);
        appointment.setDose(dose);

        entityManager.persist(appointment);
        entityManager.flush();
        return appointment;
    }


    public void deleteAppointment(Integer appointmentNumber) {
        Appointment appointment = entityManager.createQuery("From Appointment", Appointment.class).getResultList().get(appointmentNumber);
        entityManager.remove(appointment);

    }



    public Appointment editTimePattern(int id, String timePattern, int pageNumber) {
        Appointment appointment = findAll(pageNumber).get(id);
        appointment.setTimePattern(timePattern);
        entityManager.merge(appointment);
        return appointment;
    }



    public Appointment editDose(int id, double dose, int pageNumber) {
        Appointment appointment = findAll(pageNumber).get(id);
        appointment.setDose(dose);
        entityManager.merge(appointment);
        return appointment;
    }


    public Appointment editPeriod(int id, Date startDate, Date endDate, int pageNumber) {
        Appointment appointment = findAll(pageNumber).get(id);
        appointment.setStartDate(startDate);
        appointment.setEndDate(endDate);
        entityManager.merge(appointment);
        return appointment;
    }

}
