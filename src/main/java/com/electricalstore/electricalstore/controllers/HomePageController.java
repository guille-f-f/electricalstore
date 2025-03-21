package com.electricalstore.electricalstore.controllers;

import com.electricalstore.electricalstore.services.ArticleService;
import com.electricalstore.electricalstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomePageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/")
    public String homePage() {
        return "dashboard.html";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login.html";
    }

    @GetMapping("/register")
    public String howRegisterForm() {
        return "register.html";
    }

    @PostMapping("/register")
    public String handleUserRegister(@RequestParam String name, @RequestParam String lastName, @RequestParam String email, @RequestParam String password, @RequestParam String repeatPassword, ModelMap modelo) {
        userService.register(email, name, lastName, password, repeatPassword);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(ModelMap model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "index.html";
    }
}
