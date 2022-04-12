package ru.javaschool.JavaSchoolBackend2.entity;

import lombok.NoArgsConstructor;
import ru.javaschool.JavaSchoolBackend2.enums.Position;
import lombok.Data;
import javax.persistence.*;


@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private Position position;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Doctor doctor;


    public User(String name, String password, Position position) {
        this.name = name;
        this.password = password;
        this.position = position;
    }

}
