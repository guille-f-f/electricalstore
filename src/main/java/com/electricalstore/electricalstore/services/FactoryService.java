package com.electricalstore.electricalstore.services;

import com.electricalstore.electricalstore.entities.Factory;
import com.electricalstore.electricalstore.exeptions.ObjectNotFoundException;
import com.electricalstore.electricalstore.repositories.FactoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class FactoryService {

    @Autowired
    private FactoryRepository factoryRepository;

    @Transactional(readOnly = true)
    public List<Factory> getAllFactories() {
        return factoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Factory getFactoryById(UUID idFactory) {
        return getFactoryOrThrow(idFactory);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public Factory addFactory(String name) {
        Factory factory = new Factory();
        return factoryRepository.save(populateFactory(factory, name));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public Factory updateFactory(UUID id, String name) {
        Factory factory = getFactoryOrThrow(id);
        return factoryRepository.save(populateFactory(factory, name));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public String deleteFactoryById(UUID id) {
        Factory factory = getFactoryOrThrow(id);
        factoryRepository.delete(factory);
        return "Factory with ID " + id + " deleted successfully.";
    }

    // =======================
    // Private methods
    // =======================

    private Factory populateFactory(Factory factory, String name) {
        factory.setFactoryName(name);
        return factory;
    }

    private Factory getFactoryOrThrow(UUID id) {
        return factoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Factory with ID " + id + " not found."));
    }

}