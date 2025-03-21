package com.electricalstore.electricalstore.services;

import com.electricalstore.electricalstore.entities.Factory;
import com.electricalstore.electricalstore.repositories.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FactoryService {

    @Autowired
    private FactoryRepository factoryRepository;

    @Transactional(readOnly = true)
    public List<Factory> getAllFactories() {
        return factoryRepository.findAll();
    }

}