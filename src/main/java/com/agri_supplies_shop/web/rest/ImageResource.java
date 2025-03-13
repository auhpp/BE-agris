package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.ImageResponse;
import com.agri_supplies_shop.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageResource {

    ImageService imageService;

    @PostMapping
    public ApiResponse<ImageResponse> uploadImage(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return ApiResponse.<ImageResponse>builder()
                .code(200)
                .result(
                        imageService.saveImage(file)
                )
                .build();
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<UrlResource> getImage(@PathVariable String fileName) {
        return ResponseEntity.ok().contentType(
                MediaType.IMAGE_JPEG
        ).body(imageService.getImage(fileName));
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable("id") Long id) {
        imageService.deleteImg(id);
    }
}
