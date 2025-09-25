package com.more_than_code.go_con_coche.renter_profile;

import com.more_than_code.go_con_coche.cloudinary.CloudinaryService;
import com.more_than_code.go_con_coche.cloudinary.DefaultImageType;
import com.more_than_code.go_con_coche.cloudinary.UploadResult;
import com.more_than_code.go_con_coche.global.EntityAlreadyExistsException;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.services.UserAuthService;
import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileMapper;
import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileRequest;
import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileResponse;
import com.more_than_code.go_con_coche.renter_profile.models.RenterProfile;
import com.more_than_code.go_con_coche.renter_profile.models.TypeLicense;
import com.more_than_code.go_con_coche.renter_profile.services.RenterProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for UserService")
public class RenterProfileServiceTest {

    @Mock
    private  RenterProfileRepository renterProfileRepository;
    @Mock
    private  UserAuthService userAuthService;
    @Mock
    private  RenterProfileMapper renterProfileMapper;
    @Mock
    private  CloudinaryService cloudinaryService;

    @InjectMocks
    private RenterProfileServiceImpl renterProfileService;

    private RegisteredUser user;
    private RenterProfileRequest request;
    private RenterProfile renterProfile;
    private RenterProfileResponse responseDto;
    private UploadResult uploadResult;

    @BeforeEach
    void setUp() {
        user = new RegisteredUser();
        user.setUsername("testUser");
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "test.png",
                "image/png",
                "fake-image-content".getBytes()
        );
        uploadResult = new UploadResult("http://cloudinary.com/img.png", "publicId123");
        request = new RenterProfileRequest(TypeLicense.B, "abc123456", LocalDate.of(2026,11,26), mockFile);
        renterProfile = new RenterProfile();
        responseDto = new RenterProfileResponse(1L, 5L, "B", "abc123456", LocalDate.of(2026,11,26), "/images/default-profile.jpg");
    }

    @Test
    void createRenterProfile_WhenValidRequest_ShouldReturnResponse() {
        when(renterProfileMapper.toEntity(request)).thenReturn(renterProfile);
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(renterProfileRepository.existsByRegisteredUser(user)).thenReturn(false);
        when(cloudinaryService.resolveImage(request.image(), DefaultImageType.PROFILE))
                .thenReturn(uploadResult); // 👈 faltaba este stub
        when(renterProfileRepository.save(any(RenterProfile.class))).thenReturn(renterProfile);
        when(renterProfileMapper.toResponse(renterProfile)).thenReturn(responseDto);

        RenterProfileResponse result = renterProfileService.createRenterProfile(request);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(renterProfileRepository).save(renterProfile);
        verify(cloudinaryService).resolveImage(request.image(), DefaultImageType.PROFILE);
    }

    @Test
    void createRenterProfile_WhenUserAlreadyHasProfile_ShouldThrowException() {
        when(renterProfileMapper.toEntity(request)).thenReturn(renterProfile);
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(renterProfileRepository.existsByRegisteredUser(user)).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class,
                () -> renterProfileService.createRenterProfile(request));

        verify(renterProfileRepository, never()).save(any());
        verify(cloudinaryService, never()).resolveImage(any(), any());
    }
}
