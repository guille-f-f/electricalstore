package com.electricalstore.electricalstore.controllers;

import com.electricalstore.electricalstore.entities.Article;
import com.electricalstore.electricalstore.services.ArticleService;
import com.electricalstore.electricalstore.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/image")
//@PreAuthorize("isAuthenticated()")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ArticleService articleService;

//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/article/{idArticle}")
    public ResponseEntity<byte[]> getImage(@PathVariable UUID idArticle) {
        Article article = articleService.getArticleById(idArticle);
        if (article.getImage() != null && article.getImage().getContent() != null) {
            byte[] content = article.getImage().getContent();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(article.getImage().getMime()));
            return new ResponseEntity<>(content, httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}