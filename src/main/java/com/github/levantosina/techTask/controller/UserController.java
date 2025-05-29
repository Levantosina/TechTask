package com.github.levantosina.techTask.controller;


import com.github.levantosina.techTask.dto.PageDTO;

import com.github.levantosina.techTask.repository.SubscriptionNameCount;
import com.github.levantosina.techTask.requests.SubscriptionRegistrationRequest;
import com.github.levantosina.techTask.requests.UserRegistrationRequest;
import com.github.levantosina.techTask.requests.UserUpdatingRequest;
import com.github.levantosina.techTask.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller()
@AllArgsConstructor
@RequestMapping("api/v1")
@Validated
@Slf4j
public class UserController {

    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        userService.createNewUser(userRegistrationRequest);
        return ResponseEntity.ok("Card created successfully");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId,
                                        @Valid @RequestBody UserUpdatingRequest userUpdatingRequest) {
        return ResponseEntity.ok( userService.updateUser(userId, userUpdatingRequest));
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/users/{userId}/subscriptions")
    public ResponseEntity<?> creteNewSubscriptionByUserId(@PathVariable("userId") Long userId,
                                                          @Valid @RequestBody SubscriptionRegistrationRequest  subscriptionRegistrationRequest) {
        userService.createNewSubscription(userId, subscriptionRegistrationRequest);
        return ResponseEntity.ok("New subscription created successfully");
    }

    @GetMapping("/users/{userId}/subscriptions")
    public ResponseEntity<?> getSubscriptionByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getSubscriptionsByUserId(userId));
    }

    @DeleteMapping("/users/{userId}/subscriptions/{sub_id}")
    public ResponseEntity<?> deleteSubscriptionById(@PathVariable("userId") Long userId,
                                                    @PathVariable("sub_id") Long subscriptionId) {
        userService.deleteSubscription(userId,subscriptionId);
        return ResponseEntity.ok("Subscription deleted successfully");
    }

    @GetMapping("/subscription/top")
    public ResponseEntity<?> getTopSubscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Page<SubscriptionNameCount> pageResult = userService.getTop3SubscriptionsByUser(page, size);
        return ResponseEntity.ok(new PageDTO<>(pageResult));
    }
}