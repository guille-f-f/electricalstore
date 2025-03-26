package com.electricalstore.electricalstore.controllers;

import com.electricalstore.electricalstore.entities.Factory;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.electricalstore.electricalstore.services.ArticleService;
import com.electricalstore.electricalstore.services.FactoryService;

@Controller
@RequestMapping("/factory")
@PreAuthorize("isAuthenticated()")
public class FactoryController {
    @Autowired
    private FactoryService factoryService;

    @Autowired
    private ArticleService articleService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/list")
    public String showListFactory(ModelMap model) {
        model.addAttribute("factories", factoryService.getAllFactories());
        return "factory_list.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/form")
    public String showAddFactoryForm(ModelMap model) {
        model.addAttribute("factory", new Factory());
        model.addAttribute("updateMode", false);
        return "factory_form.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/form/{idFactory}")
    public String showUpdateFactoryForm(@PathVariable String idFactory, ModelMap model) {
        model.addAttribute("factory", factoryService.getFactoryById(UUID.fromString(idFactory)));
        model.addAttribute("updateMode", true);
        return "factory_form.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/save")
    public String handleSaveFactory(@RequestParam Boolean updateMode, @RequestParam(required = false) UUID idFactory,
            @RequestParam String factoryName) {
        if (updateMode) {
            factoryService.updateFactory(idFactory, factoryName);
        } else {
            factoryService.addFactory(factoryName);
        }
        return "redirect:/factory/list";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/delete/{idFactory}")
    public String handleDeleteFactory(@PathVariable String idFactory, RedirectAttributes redirectAttributes) {
        Logger logger = LoggerFactory.getLogger(getClass());

        UUID factoryId;
        try {
            factoryId = UUID.fromString(idFactory);
        } catch (IllegalArgumentException e) {
            logger.error("ID de fábrica no válido: {}", idFactory);
            redirectAttributes.addFlashAttribute("error", "ID de fábrica inválido."); // Permite mostrar mensajes de
                                                                                      // error y éxito después de la
                                                                                      // redirección.
            return "redirect:/factory/list";
        }
        String factoryName = factoryService.getFactoryById(factoryId).getFactoryName();
        if (articleService.findArticleByAssociatedFactory(factoryId)) {
            logger.warn("Intento de eliminar una fábrica con artículos asociados: {}", idFactory);
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar la fábrica " + factoryName + ", debido a que tiene artículos disponibles.");
            return "redirect:/factory/list";
        }

        factoryService.deleteFactoryById(factoryId);
        redirectAttributes.addFlashAttribute("success", "Fábrica " + factoryName + " fue eliminada exitosamente.");
        return "redirect:/factory/list";
    }

}
