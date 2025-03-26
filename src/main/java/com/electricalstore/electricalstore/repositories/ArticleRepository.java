package com.electricalstore.electricalstore.repositories;

import com.electricalstore.electricalstore.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    @Query("SELECT MAX(a.articleNumber) FROM Article a")
    Integer findMaxArticleNumber();

    @Query("SELECT COUNT(a) > 0 FROM Article a WHERE a.factory.idFactory = :idFactory")
    boolean existsArticlesByFactory(@Param("idFactory") UUID idFactory);

}
