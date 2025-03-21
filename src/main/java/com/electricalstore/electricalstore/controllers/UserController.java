package com.electricalstore.electricalstore.controllers;

import com.electricalstore.electricalstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class UserController {

    @Autowired
    private UserService userService;

}
