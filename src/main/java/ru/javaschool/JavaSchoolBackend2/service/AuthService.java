package ru.javaschool.JavaSchoolBackend2.service;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaschool.JavaSchoolBackend2.dao.UserDao;
import ru.javaschool.JavaSchoolBackend2.dto.UserDto;
import ru.javaschool.JavaSchoolBackend2.entity.User;
import ru.javaschool.JavaSchoolBackend2.security.JwtProvider;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserDao userDao;

    private final JwtProvider jwtProvider;

    private final DoctorsService doctorsService;

    private static final Logger LOGGER = LogManager.getLogger(AuthService.class);


    public UserDto registr(UserDto userDto) {
        if (userDto.getName() == null || userDto.getPassword() == null || !(userDto.getPosition().equals("DOCTOR")
                || userDto.getPosition().equals("NURSE"))) {
            return new UserDto("Bad request some parameters are missing", null);
        }
        if (userDao.findByUserName(userDto.getName()) != null) {
            return new UserDto("can't save users with equal names", null);
        }
        User currentUser = userDao.save(userDto.getName(), passwordEncoder.encode(userDto.getPassword()), userDto.getPosition());
        if (currentUser.getName() != null) {
            if(currentUser.getPosition().equals("DOCTOR")) {
                doctorsService.addDoctor(currentUser);
            }
            return new UserDto(String.valueOf(currentUser.getPosition()), currentUser.getToken());
        } else {
            LOGGER.error("something went wrong with registration");
            return new UserDto("Registration failed", null);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NoSuchAlgorithmException.class, NullPointerException.class})
    public UserDto signIn(UserDto userDto) {
        User currentUser = userDao.findByUserName(userDto.getName());
        if (currentUser == null) {
            LOGGER.warn("incorrect user data from authorization");
            return new UserDto("Login failed", null);
        } else {
            if (passwordEncoder.matches(userDto.getPassword(), currentUser.getPassword())) {
                String token = jwtProvider.generateToken(userDto.getName());
                currentUser.setToken(token);
                userDao.update(currentUser);
                return new UserDto(String.valueOf(currentUser.getPosition()), token);
            } else {
                return new UserDto("Wrong password", null);
            }


        }
    }
}
