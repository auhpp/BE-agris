package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.response.ImageResponse;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    ImageResponse saveImage(MultipartFile file
                                  ) throws IOException;
    UrlResource getImage(String fileName);
    void deleteImg(Long id);
}
