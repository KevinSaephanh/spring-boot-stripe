package com.kevin.sample.controllers;

import com.kevin.sample.dto.requests.SubscriptionRequest;
import com.kevin.sample.services.SubscriptionService;
import com.stripe.model.Subscription;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("v1/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/create")
    @ResponseBody
    public Subscription createSubscription(@RequestBody @Valid SubscriptionRequest subscriptionRequest) {
        log.info("SubscriptionController createSubscription: with: {}", subscriptionRequest);
        return subscriptionService.createSubscription(subscriptionRequest);
    }

    @DeleteMapping("/cancel/{customerId}")
    @ResponseBody
    public String cancelSubscription(@PathVariable String customerId) throws Exception {
        log.info("SubscriptionController cancelSubscription: with customer id: {}", customerId);
        return subscriptionService.cancelSubscription(customerId);
    }
}
