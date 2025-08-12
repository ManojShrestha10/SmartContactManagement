package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    // Method to upload an image
    
    String uploadImage(MultipartFile file, String fileName);

    String getUrlFromPublicId(String publicId);

}

