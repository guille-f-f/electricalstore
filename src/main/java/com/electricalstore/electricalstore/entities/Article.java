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

    @Column(name = "article_number")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_sequence")
    @SequenceGenerator(name = "article_sequence", sequenceName = "article_sequence", allocationSize = 1)
    private Integer articleNumber;
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Column(name = "article_name", nullable = false)
    private String articleName;

    @Column(name = "article_description", nullable = false)
    private String articleDescription;

    @ManyToMany
    @JoinTable(
            name = "article_factory",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "factory_id")
    )
    private List<Factory> factories;
}

