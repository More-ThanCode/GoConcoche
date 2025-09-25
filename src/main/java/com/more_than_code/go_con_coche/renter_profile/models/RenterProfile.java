package com.more_than_code.go_con_coche.renter_profile.models;

import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "renter_profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenterProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "registered_user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RegisteredUser registeredUser;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private TypeLicense typeLicense;

    @Column(name = "licence_number", nullable = false)
    private String licenseNumber;

    @CreationTimestamp
    @Column(name = "expired_date")
    public LocalDate expiredDate;

    @NotBlank
    @Column(name = "image_url")
    private String imageURL;
}
