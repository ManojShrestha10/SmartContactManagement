package com.scm.services.Impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helper.AppConstants;
import com.scm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    // autowire cloudinary configuration here
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file, String fileName) {

        //String fileName = UUID.randomUUID().toString();
        try {
            byte[] imageBytes = new byte[file.getInputStream().available()];
            file.getInputStream().read(imageBytes);
            cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                    "public_id", fileName));
            return this.getUrlFromPublicId(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String getUrlFromPublicId(String publicId) {

        return cloudinary
                .url()
                .transformation(
                        new Transformation()
                                .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                                .width(AppConstants.CONTACT_IMAGE_HEIGHT)
                                .crop(AppConstants.CONTACT_IMAGE_CROP))

                .generate(publicId);

    }

}
