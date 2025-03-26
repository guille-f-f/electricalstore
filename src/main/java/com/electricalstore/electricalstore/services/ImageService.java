package com.electricalstore.electricalstore.services;

import com.electricalstore.electricalstore.entities.Image;
import com.electricalstore.electricalstore.exeptions.ObjectNotFoundException;
import com.electricalstore.electricalstore.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@PreAuthorize("isAuthenticated()")
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public Image getImageById(UUID id) {
        return getImageOrThrow(id);
    }

    @Transactional
    public Image addImage(MultipartFile file) throws IOException {
        validateFile(file);
        Image image = populateImage(new Image(), file);
        return imageRepository.save(image);
    }

    @Transactional
    public Image updateImage(UUID id, MultipartFile file) throws IOException {
        validateFile(file);
        Image image = getImageOrThrow(id);
        populateImage(image, file);
        return imageRepository.save(image);
    }

    @Transactional
    public Image updateImage(UUID id, String name, String mime, String contentType, byte[] content) throws IOException {
        Image image = getImageOrThrow(id);
        return imageRepository.save(image);
    }

    // =======================
    // Private methods
    // =======================

    private Image populateImage(Image image, MultipartFile file) throws IOException {
        image.setName(file.getOriginalFilename());
        image.setMime(file.getContentType());
        image.setContent(file.getBytes());
        return image;
    }

    private Image getImageOrThrow(UUID id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Image with ID " + id + " not found."));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty.");
        }
    }
}
