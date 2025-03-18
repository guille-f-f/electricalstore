package com.electricalstore.electricalstore.repositories;

import com.electricalstore.electricalstore.entities.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, UUID> {
}
