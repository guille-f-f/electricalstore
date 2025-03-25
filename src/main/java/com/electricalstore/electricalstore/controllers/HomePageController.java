package com.electricalstore.electricalstore.controllers;

import com.electricalstore.electricalstore.entities.User;
import com.electricalstore.electricalstore.services.ArticleService;
import com.electricalstore.electricalstore.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public String showIndex(ModelMap model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "index.html";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login.html";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register.html";
    }

    @PostMapping("/register")
    public String handleUserRegister(@RequestParam String name, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password, @RequestParam String repeatPassword,
            ModelMap modelo) {
        User user = userService.register(email, name, lastName, password, repeatPassword);
        System.out.println("ROLE DE USUARIO ALMACENADO: " + user.getRol());
        return "redirect:/login";
    }

    @GetMapping("/index")
    public String redirectUserLogged(HttpSession session) {
        User user = (User) session.getAttribute("userSession");
        if (user.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/homepage";
    }

    @PreAuthorize("HasAnyRole('USER', 'ADMIN')")
    @GetMapping("/homepage")
    public String showDashboard() {
        return "homepage.html";
    }
}
