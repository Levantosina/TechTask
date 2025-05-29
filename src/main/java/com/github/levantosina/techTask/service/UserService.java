package com.github.levantosina.techTask.service;

import com.github.levantosina.techTask.dto.SubscriptionDTO;
import com.github.levantosina.techTask.dto.UserDTO;
import com.github.levantosina.techTask.exception.RequestValidationException;
import com.github.levantosina.techTask.mapper.SubscriptionDTOMapper;
import com.github.levantosina.techTask.mapper.UserDTOMapper;
import com.github.levantosina.techTask.model.SubscriptionStatus;
import com.github.levantosina.techTask.model.SubscriptionsEntity;
import com.github.levantosina.techTask.model.UserEntity;
import com.github.levantosina.techTask.repository.SubscriptionNameCount;
import com.github.levantosina.techTask.repository.SubscriptionRepository;
import com.github.levantosina.techTask.repository.UserRepository;
import com.github.levantosina.techTask.requests.SubscriptionRegistrationRequest;
import com.github.levantosina.techTask.requests.UserRegistrationRequest;
import com.github.levantosina.techTask.exception.ResourceNotFoundException;
import com.github.levantosina.techTask.requests.UserUpdatingRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserDTOMapper userDTOMapper;
    private final SubscriptionDTOMapper subscriptionDTOMapper;


    @Transactional
    public void createNewUser(UserRegistrationRequest registrationRequest) {
        log.info("Trying to register user with email: {}", registrationRequest.email());
        if (userRepository.existsByEmail(registrationRequest.email())) {
            log.warn("Email already taken: {}", registrationRequest.email());
            throw new RequestValidationException("Email [%s] is already taken".formatted(registrationRequest.email()));
        }

        UserEntity userEntity = UserEntity.builder()
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .email(registrationRequest.email())
                .build();
        userRepository.save(userEntity);
        log.info("User registered successfully with email: {}", registrationRequest.email());
    }
    @Transactional
    public UserDTO getUserById(Long id) {
        log.info("Fetching user with id {}  ", id);
        return userRepository.findById(id)
                .map(userDTOMapper)
                .orElseThrow(()-> {
                    log.warn("User with id {} not found", id);
                  return new ResourceNotFoundException("User with id [%s] not found".formatted(id));
                });
    }
    @Transactional
    public Page<SubscriptionNameCount> getTop3SubscriptionsByUser(int page, int size) {
        log.info("Fetching top {}  (page: {})", size, page);
        Pageable pageable = PageRequest.of(page, size);
        Page<SubscriptionNameCount> result = subscriptionRepository.findMostPopularSubscriptions(pageable);
        log.debug("Top subscriptions fetched, count: {}", result.getTotalElements());
        return result;
    }

    @Transactional
    public UserDTO updateUser(Long userId, UserUpdatingRequest userUpdatingRequest) {
        log.info("Updating user with id: {}", userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->
                {
                    log.warn("User with id {} not found for update", userId);
                   return new ResourceNotFoundException("User with [%s] not found".formatted(userId));
                });

        if (userUpdatingRequest.email() != null && !userUpdatingRequest.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userUpdatingRequest.email())) {
                log.warn("Email {} is already taken", userUpdatingRequest.email());
                throw new IllegalArgumentException("Email address is already taken.");
            }
            user.setEmail(userUpdatingRequest.email());
        }

        boolean changes = false;

        if (userUpdatingRequest.firstName() != null && !userUpdatingRequest.firstName().equals(user.getFirstName())) {
            user.setFirstName(userUpdatingRequest.firstName());
            changes = true;
        }

        if (userUpdatingRequest.lastName() != null && !userUpdatingRequest.lastName().equals(user.getLastName())) {
            user.setLastName(userUpdatingRequest.lastName());
            changes = true;
        }

        if (userUpdatingRequest.email() != null && !userUpdatingRequest.email().equals(user.getEmail())) {
            user.setEmail(userUpdatingRequest.email());
            changes = true;
        }
        if (!changes) {
            log.info("No changes detected for user with id: {}", userId);
            throw new RequestValidationException("No changes for id with [%s] detected".formatted(userId));
        }
        UserEntity updated = userRepository.save(user);
        log.info("User with id {} successfully updated", userId);
        return userDTOMapper.apply(updated);
    }

  @Transactional
    public void deleteUser(Long userId) {
      log.info("Delete user with id: {}", userId);
        if(!userRepository.existsById(userId)) {
            log.warn("User with id {} not found for deletion", userId);
            throw new ResourceNotFoundException("User with id [%s] not found".formatted(userId));
        }
        userRepository.deleteById(userId);
        log.info("User with id {} successfully deleted", userId);
  }

  @Transactional
    public void createNewSubscription(Long userId, SubscriptionRegistrationRequest subscriptionRegistrationRequest) {

      log.info("Creating a new subscription for userId: {}", userId);

      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> {
                  log.warn("User with id {} not found for subscription", userId);
                  return new ResourceNotFoundException("User with id [%s] not found".formatted(userId));
              });

      SubscriptionStatus status = subscriptionRegistrationRequest.endDate().isBefore(LocalDate.now())
              ? SubscriptionStatus.EXPIRED
              : SubscriptionStatus.ACTIVE;

      SubscriptionsEntity subscription = SubscriptionsEntity.builder()
              .subscriptionName(subscriptionRegistrationRequest.subscriptionName())
              .monthlyPrice(subscriptionRegistrationRequest.monthlyPrice())
              .endDate(subscriptionRegistrationRequest.endDate())
              .subscriptionStatus(status)
              .user(user)
              .build();

      subscriptionRepository.save(subscription);
      log.info("Subscription created for userId {} with name {}", userId, subscription.getSubscriptionName());

  }

    @Transactional
    public List<SubscriptionDTO> getSubscriptionsByUserId(Long userId) {
        log.info("Fetching subscriptions for userId: {}", userId);
        List<SubscriptionsEntity> subscriptions = subscriptionRepository.findAllByUser_UserId(userId);

        if (subscriptions.isEmpty()) {
            log.warn("No subscriptions found for userId: {}", userId);
            throw new ResourceNotFoundException("No subscriptions found for user with id [%s]".formatted(userId));
        }
        log.info("Found {} subscriptions for userId: {}", subscriptions.size(), userId);
        return subscriptions.stream()
                .map(subscriptionDTOMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSubscription(Long userId, Long subscriptionId) {
        log.info("Delete subscription with id {} for user {}", subscriptionId, userId);

        SubscriptionsEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> {
                    log.warn("Subscription with id {} not found", subscriptionId);
                   return new ResourceNotFoundException(
                            "Subscription with id [%s] not found".formatted(subscriptionId));
                });

        if (!subscription.getUser().getUserId().equals(userId)) {
            log.warn("Subscription with id {} does not belong to user {}", subscriptionId, userId);
            throw new RequestValidationException("Subscription does not belong to user with id [%s]".formatted(userId));
        }
        log.info("Subscription with id {} successfully deleted for user {}", subscriptionId, userId);
        subscriptionRepository.deleteById(subscriptionId);
    }

}
