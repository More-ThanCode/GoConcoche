package com.more_than_code.go_con_coche.owner_profile;

import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table (name="owner_profiles")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OwnerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "registered_user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RegisteredUser registeredUser;

    @NotBlank
    @Column(name = "image_url", nullable = false)
    private String imageURL;
}