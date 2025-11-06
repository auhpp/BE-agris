package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.dto.response.ImageResponse;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.ImageRepository;
import com.agri_supplies_shop.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceImpl implements ImageService {

    @NonFinal
    @Value("${file.upload-dir}")
    String uploadDir;

    ImageRepository imageRepository;

    @Override
    @Transactional
    public ImageResponse saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // file format validation
        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            new AppException(ErrorCode.FILE_NOT_SUPPORTED);
        }

        String timeStampedFileName = new SimpleDateFormat("ssmmHHddMMyyyy")
                .format(new Date()) + "_" + file.getOriginalFilename();

        Path filePath = uploadPath.resolve(timeStampedFileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/").path(timeStampedFileName).toUriString();

        return ImageResponse.builder()
                .filePath(
                        fileUri
                ).build();
    }

    @Override
    public UrlResource getImage(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteImg(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteImgLocal(String name) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        Path filePath = uploadPath.resolve(name);
        Files.delete(filePath);
    }
}
