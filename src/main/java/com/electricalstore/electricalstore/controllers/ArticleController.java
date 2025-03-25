package com.electricalstore.electricalstore.controllers;

import com.electricalstore.electricalstore.entities.Article;
import com.electricalstore.electricalstore.services.ArticleService;
import com.electricalstore.electricalstore.services.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/article")
@PreAuthorize("isAuthenticated()")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private FactoryService factoryService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/list")
    public String getAllArticles(ModelMap model) {
        List<Article> articles = articleService.getAllArticles();
        articles.sort(Comparator.comparingInt(Article::getArticleNumber));
        model.addAttribute("articles", articles);
        return "article_list.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/form")
    public String showAddArticleForm(ModelMap model) {
        model.addAttribute("article", new Article());
        model.addAttribute("factories", factoryService.getAllFactories());
        model.addAttribute("updateMode", false);
        return "article_form.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/form/{idArticle}")
    public String showUpdateArticleForm(@PathVariable String idArticle, ModelMap model) {
        model.addAttribute("article", articleService.getArticleById(UUID.fromString(idArticle)));
        model.addAttribute("factories", factoryService.getAllFactories());
        model.addAttribute("updateMode", true);
        return "article_form.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping("/add")
    public String handleAddArticle(@RequestParam Boolean updateMode, @RequestParam(required = false) UUID idArticle,
            @RequestParam String articleName, @RequestParam String articleDescription, @RequestParam UUID idFactory,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        if (updateMode) {
            articleService.updateArticle(idArticle, articleName, articleDescription, idFactory, file);
        } else {
            articleService.addArticle(articleName, articleDescription, idFactory, file);
        }
        return "redirect:/article/list";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/delete/{idArticle}")
    public String handleDeleteArticle(@PathVariable UUID idArticle) {
        articleService.deleteArticleById(idArticle);
        return "redirect:/article/list";
    }

}
