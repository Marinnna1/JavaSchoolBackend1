package ru.javaschool.JavaSchoolBackend2.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.javaschool.JavaSchoolBackend2.dto.UserDto;
import ru.javaschool.JavaSchoolBackend2.security.JwtProvider;
import ru.javaschool.JavaSchoolBackend2.service.AuthService;



@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("user/")
@RequiredArgsConstructor
public class UserController {


    private final AuthService userService;


    @PostMapping(path="login")
    public UserDto doSignIn(@RequestBody UserDto userDto) {
        try {
            return userService.signIn(userDto);
        }
        catch (Exception e){
            e.printStackTrace();
            return new UserDto("Transaction rollback Login failed", null);
        }
    }


    @PostMapping("reg")
    public UserDto doSignUp(@RequestBody UserDto userDto){
        return  userService.registr(userDto);
    }

}
