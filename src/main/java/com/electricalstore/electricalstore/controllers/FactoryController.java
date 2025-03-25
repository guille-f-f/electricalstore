package com.electricalstore.electricalstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.electricalstore.electricalstore.services.FactoryService;

@Controller
@RequestMapping("/factory")
@PreAuthorize("isAuthenticated()")
public class FactoryController {
    @Autowired
    private FactoryService factoryService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/list")
    public String showListFactory(ModelMap model) {
        model.addAttribute("factories", factoryService.getAllFactories());
        return "factory_list.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/form")
    public String showAddFactoryForm() {
        return "factory_form.html";
    }

}
