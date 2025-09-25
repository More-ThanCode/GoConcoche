package com.more_than_code.go_con_coche.location.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LocationRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validLocationRequest_success() {
        LocationRequest request = new LocationRequest("Madrid", "Centro");

        Set<ConstraintViolation<LocationRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void blankCity_NoValidation() {
        LocationRequest request = new LocationRequest(" ", "Centro");

        Set<ConstraintViolation<LocationRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("city"));
    }

    @Test
    void tooShortDistrict_NoValidation() {
        LocationRequest request = new LocationRequest("Madrid", "A");

        Set<ConstraintViolation<LocationRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("district"));
    }


    @Test
    void district_shouldBeValid() {
        LocationRequest request = new LocationRequest("San Sebastian", "Donostia-Kursaal");
        Set<ConstraintViolation<LocationRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }
}