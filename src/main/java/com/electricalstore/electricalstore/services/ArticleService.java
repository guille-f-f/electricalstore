package com.electricalstore.electricalstore.services;

import com.electricalstore.electricalstore.entities.Article;
import com.electricalstore.electricalstore.entities.Factory;
import com.electricalstore.electricalstore.exeptions.ObjectNotFoundException;
import com.electricalstore.electricalstore.repositories.ArticleRepository;
import com.electricalstore.electricalstore.repositories.FactoryRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
// @PreAuthorize("isAuthenticated()")
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FactoryRepository factoryRepository;

    @Autowired
    private ImageService imageService;

    @Transactional(readOnly = true)
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Article getArticleById(UUID id) {
        return getArticleOrThrow(id);
    }

    // @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public Article addArticle(String name, String description, UUID idFactory) throws IOException {
        Article article = new Article();
        return articleRepository.save(populateArticle(article, name, description, idFactory, null));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public Article addArticle(String name, String description, UUID idFactory, MultipartFile file) throws IOException {
        Article article = new Article();
        return articleRepository.save(populateArticle(article, name, description, idFactory, file));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public void addImageToArticle(UUID id, MultipartFile file) throws IOException {
        validateFile(file);
        Article article = getArticleOrThrow(id);
        if (article.getImage() == null) {
            article.setImage(imageService.addImage(file));
        } else {
            article.setImage(imageService.updateImage(article.getImage().getId(), file));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public Article updateArticle(UUID id, String name, String description, UUID idFactory, MultipartFile file)
            throws IOException {
        // validateFile(file);
        System.out.println(id);
        Article article = getArticleOrThrow(id);
        return articleRepository.save(populateArticle(article, name, description, idFactory, file));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public String deleteArticleById(UUID id) {
        Article article = getArticleOrThrow(id);
        articleRepository.delete(article);
        return "Article with ID " + id + " deleted successfully.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Boolean findArticleByAssociatedFactory(UUID idFactory) {
        return articleRepository.existsArticlesByFactory(idFactory);
    }

    // =======================
    // Private methods
    // =======================

    private Article populateArticle(Article article, String name, String description, UUID idFactory,
            @Nullable MultipartFile file) throws IOException {
        if (article.getArticleNumber() == null) {
            article.setArticleNumber(generateNextArticleNumber());
        }
        article.setArticleName(name);
        article.setArticleDescription(description);
        Factory factory = factoryRepository.findById(idFactory)
                .orElseThrow(() -> new ObjectNotFoundException("Factory with ID " + idFactory + " not found."));
        article.setFactory(factory);
        if (file != null && !file.isEmpty()) {
            validateFile(file);
            article.setImage(imageService.addImage(file));
        }
        return article;
    }

    private Article getArticleOrThrow(UUID id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Article with ID " + id + " not found."));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty.");
        }
    }

    private Integer generateNextArticleNumber() {
        Integer max = articleRepository.findMaxArticleNumber();
        return max != null ? max + 1 : 1;
    }
}