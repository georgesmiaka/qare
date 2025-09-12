package com.qare.app.model;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MedicalSupplyTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validMedicalSupply_hasNoViolations() {
        var ms = new MedicalSupply("Gauze", 10, "pack");
        Set<ConstraintViolation<MedicalSupply>> violations = validator.validate(ms);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void name_mustNotBeBlank(String name) {
        var ms = new MedicalSupply(name, 1, "pack");
        Set<ConstraintViolation<MedicalSupply>> violations = validator.validate(ms);
        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("name"));
    }

    @Test
    void amount_mustBeNonNegative() {
        var ms = new MedicalSupply("Gauze", -1, "pack");
        Set<ConstraintViolation<MedicalSupply>> violations = validator.validate(ms);
        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("amount"));
    }

    @Test
    void amount_zero_isValid() {
        var ms = new MedicalSupply("Gauze", 0, "pack");
        Set<ConstraintViolation<MedicalSupply>> violations = validator.validate(ms);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void unitName_mustNotBeBlank(String unit) {
        var ms = new MedicalSupply("Gauze", 1, unit);
        Set<ConstraintViolation<MedicalSupply>> violations = validator.validate(ms);
        assertThat(violations)
                .anySatisfy(v -> assertThat(v.getPropertyPath().toString()).isEqualTo("unitName"));
    }
}

