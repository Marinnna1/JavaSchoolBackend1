package ru.javaschool.JavaSchoolBackend2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Integer id;
    private String name;
    private String password;
    private String position;
    private String token;

    public UserDto(Integer id, String name, String password, String position) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.position = position;
    }

    public UserDto(String position, String token) {
        this.position = position;
        this.token = token;
    }

}
