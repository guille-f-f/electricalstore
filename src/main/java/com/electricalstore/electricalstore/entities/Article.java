package com.electricalstore.electricalstore.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.UUID;

@Entity
@Table(name = "article")
@Data
public class Article {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_article", updatable = false, nullable = false)
    private UUID idArticle;

    @Column(name = "article_number", unique = true)
    private Integer articleNumber;

    @Column(name = "article_name", nullable = false)
    private String articleName;

    @Column(name = "article_description", nullable = false)
    private String articleDescription;

    @ManyToOne
    private Factory factory;

    @OneToOne
    private Image image;

}

