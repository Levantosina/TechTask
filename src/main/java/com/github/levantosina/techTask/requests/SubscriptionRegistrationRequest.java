package com.github.levantosina.techTask.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;


public record SubscriptionRegistrationRequest(
        @NotBlank(message = "Subscription name is required and cannot be empty")
        String subscriptionName,
        @NotNull(message = "Monthly Price is required")
        @Positive(message = "Monthly price must be positive")
        BigDecimal monthlyPrice,
        @NotNull(message = "End date name date is required")
        LocalDate endDate
) {
}
