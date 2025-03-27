package com.electricalstore.electricalstore.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.electricalstore.electricalstore.entities.User;
import com.electricalstore.electricalstore.enums.Rol;
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
        model.addAttribute("roles", List.of(Rol.ADMIN, Rol.USER));
        model.put("updateMode", false);
        return "user_form.html";
    }

    @GetMapping("/form/{id}")
    public String showUpdateUserForm(@PathVariable UUID id, ModelMap model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", List.of(Rol.ADMIN, Rol.USER));
        model.put("updateMode", true);
        return "user_form.html";
    }

    @PostMapping("/save")
    public String saveUser(@RequestParam(required = false) UUID id, @RequestParam String name,
            @RequestParam String lastName,
            @RequestParam String email, @RequestParam Rol rol, @RequestParam(required = false) String password,
            @RequestParam(required = false) String repeatPassword, @RequestParam Boolean updateMode,
            RedirectAttributes redirectAttributes) {
        if (updateMode) {
            try {
                userService.updateUser(id, email, name, lastName, rol);
                redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error",
                        "No se ha podido actualiza el usuario: " + e.getMessage());
            }
        } else {
            try {
                userService.register(email, name, lastName, password, repeatPassword);
                redirectAttributes.addFlashAttribute("success", "Usuario registrado correctamente");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error",
                        "No se ha podido registrar el usuario: " + e.getMessage());
            }
        }
        return "redirect:/user/list";
    }

}
