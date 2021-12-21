package com.milk.restfilelogger.controller;

import com.milk.restfilelogger.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jack Milk
 */

@Controller
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public UserEntity login() {
        return null;
    }


}
