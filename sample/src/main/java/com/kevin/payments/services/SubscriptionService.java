package com.kevin.payments.services;

import com.kevin.payments.dao.requests.SubscriptionRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import com.stripe.param.PaymentMethodCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SubscriptionService {
    @Value("${stripe.key}")
    private String stripeKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }

    public Subscription createSubscription(SubscriptionRequest subscriptionRequest) {
        try {
            log.info("SubscriptionService createSubscription: creating with: {}", subscriptionRequest);
            Customer customer = createCustomer(subscriptionRequest);
            PaymentMethod paymentMethod = createPaymentMethod(customer.getId(), subscriptionRequest);
            Plan plan = Plan.retrieve(subscriptionRequest.getPlanId());

            Map<String, Object> item = Map.of("plan", plan);
            List<Object> data = List.of(item);
            Map<String, Object> items = new HashMap<>();
            items.put("data", data);

            Map<String, Object> subParams = Map.of(
                    "customer", customer.getId(),
                    "default_payment_method", paymentMethod.getId(),
                    "items", items
            );
            Subscription subscription = Subscription.create(subParams);
            log.info("SubscriptionService createSubscription: success: {}", subscription);
            return subscription;
        } catch (StripeException e) {
            log.error("SubscriptionService createSubscription: fail with {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public String cancelSubscription(String customerId) {
        try {
            log.info("SubscriptionService cancelSubscription: retrieving subscriptions for: {}", customerId);
            Customer customer = Customer.retrieve(customerId);
            List<Subscription> subscriptions = customer.getSubscriptions().getData();

            if (subscriptions.isEmpty()) {
                log.info("SubscriptionService cancelSubscription: no subscriptions for: {}", customerId);
                return "No subscriptions for this customer";
            }

            Subscription sub = Subscription.retrieve(subscriptions.get(0).getId());
            sub.cancel();
            log.info("SubscriptionService cancelSubscription: success for: {}", customerId);
            return "Subscription canceled";
        } catch (StripeException e) {
            log.error("SubscriptionService cancelSubscription: fail with {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private PaymentMethod createPaymentMethod(String customerId, SubscriptionRequest subscriptionRequest) {
        try {
            PaymentMethodCreateParams params = PaymentMethodCreateParams.builder()
                    .setType(PaymentMethodCreateParams.Type.CARD)
                    .setCard(
                            PaymentMethodCreateParams.CardDetails.builder()
                                    .setNumber(subscriptionRequest.getCardNumber())
                                    .setExpMonth(Long.parseLong(subscriptionRequest.getExpMonth()))
                                    .setExpYear(Long.parseLong(subscriptionRequest.getExpYear()))
                                    .setCvc(subscriptionRequest.getCvc())
                                    .build()
                    )
                    .setCustomer(customerId)
                    .build();
            PaymentMethod paymentMethod = PaymentMethod.create(params);
            log.info("SubscriptionService createPaymentMethod: success: {}", paymentMethod);
            return paymentMethod;
        } catch (StripeException e) {
            log.error("SubscriptionService createPaymentMethod: fail with {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private Customer createCustomer(SubscriptionRequest subscriptionRequest) {
        try {
            Map<String, Object> customerMap = Map.of(
                    "name", subscriptionRequest.getUsername(),
                    "email", subscriptionRequest.getEmail()
            );
            Customer customer = Customer.create(customerMap);
            log.info("SubscriptionService createCustomer: success: {}", customer);
            return customer;
        } catch (StripeException e) {
            log.error("SubscriptionService createCustomer: fail with {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
