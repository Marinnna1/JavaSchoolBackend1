package ru.javaschool.JavaSchoolBackend2.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaschool.JavaSchoolBackend2.entity.Doctor;
import ru.javaschool.JavaSchoolBackend2.entity.User;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.Doc;
import java.util.List;


@Repository
@Transactional
public class DoctorDao {

    @PersistenceContext
    private EntityManager entityManager;


    public List<Doctor> findDoctorByName(String name) {
        return entityManager
                .createQuery("From Doctor where user.name = \'" + name + "\'", Doctor.class).getResultList();
    }


    public void save(User user) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        entityManager.persist(doctor);
        entityManager.flush();

    }



}
