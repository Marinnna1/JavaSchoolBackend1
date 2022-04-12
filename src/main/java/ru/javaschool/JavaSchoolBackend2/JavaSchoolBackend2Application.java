package ru.javaschool.JavaSchoolBackend2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JavaSchoolBackend2Application {

	public static void main(String[] args) {
		SpringApplication.run(JavaSchoolBackend2Application.class, args);
	}

}
