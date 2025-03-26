package com.electricalstore.electricalstore.controllers;

import com.electricalstore.electricalstore.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminController {

    @Autowired
    private ArticleService articleService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/dashboard")
    public String showAdminDashboard(ModelMap model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "admin_homepage.html";
    }

}
