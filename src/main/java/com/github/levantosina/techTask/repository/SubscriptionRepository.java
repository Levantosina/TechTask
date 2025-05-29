package com.github.levantosina.techTask.repository;

import com.github.levantosina.techTask.model.SubscriptionsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SubscriptionRepository extends JpaRepository<SubscriptionsEntity, Long> {

    List<SubscriptionsEntity>findAllByUser_UserId(Long userId);
    @Query("SELECT s.subscriptionName AS subscriptionName, COUNT(s) AS count " +
            "FROM SubscriptionsEntity s " +
            "GROUP BY s.subscriptionName " +
            "ORDER BY count DESC")
    Page<SubscriptionNameCount> findMostPopularSubscriptions(Pageable pageable);

}
