package com.more_than_code.go_con_coche.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {
    @Mock
    private Cloudinary cloudinary;
    @Mock
    private Uploader uploader;
    private CloudinaryProperties properties;
    @InjectMocks
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        properties = new CloudinaryProperties();
        properties.setDefaultImages(Map.of(
                DefaultImageType.PROFILE, "/images/default-profile.jpg",
                DefaultImageType.CAR, "/images/default-car.jpg"
        ));
        cloudinaryService = new CloudinaryService(cloudinary, properties);

        lenient().when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void upload_whenUploadSucceeds() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());
        Map<String, Object> response = new HashMap<>();
        response.put("secure_url", "http://cloudinary.com/test.jpg");
        response.put("public_id", "vehicles/test123");

        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(response);

        UploadResult result = cloudinaryService.upload(file, "vehicles");

        assertThat(result.url()).isEqualTo("http://cloudinary.com/test.jpg");
        assertThat(result.publicId()).isEqualTo("vehicles/test123");
    }

    @Test
    void upload_whenUploadFails() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("Cloudinary down"));

        assertThatThrownBy(() -> cloudinaryService.upload(file, "vehicles"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to upload image to Cloudinary");
    }

    @Test
    void delete_success() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("result", "ok");
        when(uploader.destroy(eq("vehicles/test123"), anyMap())).thenReturn(response);

        boolean result = cloudinaryService.delete("vehicles/test123");

        assertThat(result).isTrue();
    }

    @Test
    void delete_NoSuccess() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("result", "not found");
        when(uploader.destroy(eq("vehicles/test123"), anyMap())).thenReturn(response);

        boolean result = cloudinaryService.delete("vehicles/test123");

        assertThat(result).isFalse();
    }

    @Test
    void uploadDefault_success() {
        UploadResult result = cloudinaryService.uploadDefault(DefaultImageType.CAR);

        assertThat(result.url()).isEqualTo("/images/default-car.jpg");
        assertThat(result.publicId()).isNull();
    }

    @Test
    void uploadDefault_NoSuccess() {
        CloudinaryService service = new CloudinaryService(cloudinary, new CloudinaryProperties());

        assertThatThrownBy(() -> service.uploadDefault(DefaultImageType.PROFILE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No default image configured");
    }

    @Test
    void resolveImage_shouldUpload_whenImageProvided() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());
        Map<String, Object> response = new HashMap<>();
        response.put("secure_url", "http://cloudinary.com/test.jpg");
        response.put("public_id", "vehicles/test123");

        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(response);

        UploadResult result = cloudinaryService.resolveImage(file, DefaultImageType.CAR);

        assertThat(result.url()).isEqualTo("http://cloudinary.com/test.jpg");
    }

    @Test
    void resolveImage_shouldReturnDefault_whenImageIsNull() {
        UploadResult result = cloudinaryService.resolveImage(null, DefaultImageType.CAR);

        assertThat(result.url()).isEqualTo("/images/default-car.jpg");
    }

    @Test
    void resolveImage_shouldReturnDefault_whenUploadFails() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("fail"));

        UploadResult result = cloudinaryService.resolveImage(file, DefaultImageType.CAR);

        assertThat(result.url()).isEqualTo("/images/default-car.jpg");
    }
}