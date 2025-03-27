package com.electricalstore.electricalstore.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.electricalstore.electricalstore.entities.User;
import com.electricalstore.electricalstore.services.UserService;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String showUserList(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user_list.html";
    }

    @GetMapping("/form")
    public String showCreateUserForm(ModelMap model) {
        model.addAttribute("user", new User());
        model.put("updateMode", false);
        return "user_form.html";
    }
    
    @GetMapping("/form/{id}")
    public String showUpdateUserForm(@PathVariable UUID id, ModelMap model) {
        model.addAttribute("user", userService.getUserById(id));
        model.put("updateMode", true);
        return "user_form.html";
    }

}
