package com.electricalstore.electricalstore.repositories;

import com.electricalstore.electricalstore.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    @Query("SELECT MAX(a.articleNumber) FROM Article a")
    Integer findMaxArticleNumber();

}
