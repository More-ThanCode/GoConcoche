package com.more_than_code.go_con_coche.vehicle.models;

import com.more_than_code.go_con_coche.renter_profile.models.TypeLicense;
import lombok.*;

@Getter
@RequiredArgsConstructor
public enum Seater {
    SMART(2, "2 seats, compact car", TypeLicense.B),
    SEDAN(5, "5 seats, standard car", TypeLicense.B),
    SUV(5, "5-7 seats, sport utility vehicle", TypeLicense.BE),
    VAN(7, "7+ seats, large capacity vehicle", TypeLicense.C1);

    private final int seatCount;
    private final String description;
    private final TypeLicense requiredLicense;

    public static int getSeatCount(Seater seater) {
        return seater.seatCount;
    }
}