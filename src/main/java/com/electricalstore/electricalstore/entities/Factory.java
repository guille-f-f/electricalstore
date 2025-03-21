package com.electricalstore.electricalstore.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "factory")
@Data
public class Factory {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_factory", updatable = false, nullable = false)
    private UUID idFactory;

    @Column(name = "factory_name", nullable = false)
    private String factoryName;

}
