package ru.javaschool.JavaSchoolBackend2.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors", schema = "db")
@Data
@EqualsAndHashCode(exclude="user")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToMany(mappedBy = "doctors")
    private List<Patient> patients = new ArrayList<>();


}
