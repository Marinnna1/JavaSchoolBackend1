package ru.javaschool.JavaSchoolBackend2.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaschool.JavaSchoolBackend2.dao.DoctorDao;

import ru.javaschool.JavaSchoolBackend2.entity.User;


@Service
@RequiredArgsConstructor
public class DoctorsService {

    private final DoctorDao doctorDao;


    void addDoctor(User user) {
        doctorDao.save(user);
    }
}
