package com.github.levantosina.techTask.dto;

import com.github.levantosina.techTask.model.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;


public record SubscriptionDTO (
        String subscriptionName,
        BigDecimal monthlyPrice,
        SubscriptionStatus status,
        LocalDate endDate
){
}
