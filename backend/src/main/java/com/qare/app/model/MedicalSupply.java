package com.qare.app.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MedicalSupply(
        @NotBlank String name,
        @Min(0) int amount,
        @NotBlank String unitName
) {}
