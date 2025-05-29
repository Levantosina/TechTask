package com.github.levantosina.techTask.mapper;

import com.github.levantosina.techTask.dto.SubscriptionDTO;
import com.github.levantosina.techTask.model.SubscriptionsEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SubscriptionDTOMapper implements Function<SubscriptionsEntity, SubscriptionDTO> {

    @Override
    public SubscriptionDTO apply(SubscriptionsEntity subscriptionsEntity) {
        return new  SubscriptionDTO(
                subscriptionsEntity.getSubscriptionName(),
                subscriptionsEntity.getMonthlyPrice(),
                subscriptionsEntity.getSubscriptionStatus(),
                subscriptionsEntity.getEndDate()
        );
    }
}
