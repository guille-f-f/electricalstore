package com.electricalstore.electricalstore.controllers;

import com.electricalstore.electricalstore.entities.Article;
import com.electricalstore.electricalstore.services.ArticleService;
import com.electricalstore.electricalstore.services.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/article")
//@PreAuthorize("isAutheticated()")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private FactoryService factoryService;

//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/list")
    public String getAllArticles(ModelMap model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "article_list.html";
    }

    @GetMapping("/form")
    public String showAddArticleForm(ModelMap model) {
        model.addAttribute("factories", factoryService.getAllFactories());
        return "article_form.html";
    }

    @GetMapping("/form/{idArticle}")
    public String showAddArticleForm(@RequestParam UUID idArticle, ModelMap model) {
        model.addAttribute("article", articleService.getArticleById(idArticle))
        model.addAttribute("factories", factoryService.getAllFactories());
        return "article_form.html";
    }

    @RequestMapping("/add")
    public String handleAddArticle(@RequestParam String name, @RequestParam String description, @RequestParam String idFactory, @RequestParam(required = false) MultipartFile file) throws IOException {
        System.out.println("PREMETODO");
        articleService.addArticle(name, description, UUID.fromString(idFactory), file);
        System.out.println("PostMETODO");
        return "redirect:/list";
    }

    @GetMapping("/update/{idArticle}")
    public String showUpdateArticleForm(@RequestParam UUID idArticle) {
        return "redirect:/form/" + idArticle;
    }

}
