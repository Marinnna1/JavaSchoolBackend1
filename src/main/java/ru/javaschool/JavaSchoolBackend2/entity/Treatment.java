package ru.javaschool.JavaSchoolBackend2.entity;



import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "treatments")
@Data
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;


}

