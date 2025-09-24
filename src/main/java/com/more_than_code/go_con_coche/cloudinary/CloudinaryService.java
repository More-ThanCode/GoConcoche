package com.more_than_code.go_con_coche.cloudinary;

import com.cloudinary.Cloudinary;
import com.more_than_code.go_con_coche.config.CloudinaryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final CloudinaryProperties properties;

    public UploadResult upload(MultipartFile file, String folder) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", folder);

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);

            return new UploadResult(
                    result.get("secure_url").toString(),
                    result.get("public_id").toString()
            );
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }
    }

    public boolean delete(String publicId) {
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, Map.of());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image from Cloudinary", e);
        }
    }

    public UploadResult uploadDefault(DefaultImageType type) {
        String url = properties.getDefaultImages().get(type);
        if (url == null) {
            throw new IllegalArgumentException("No default image configured for type: " + type);
        }
        return new UploadResult(url, null);
    }
}
