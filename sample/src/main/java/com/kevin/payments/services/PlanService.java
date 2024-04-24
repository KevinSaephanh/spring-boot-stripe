package com.kevin.payments.services;

import com.kevin.payments.dao.requests.PlanRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PlanService {
    @Value("${stripe.key}")
    private String stripeKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }

    public List<Plan> getAllPlans() {
        try {
            Map<String, Object> planParams = Map.of("limit", "10");
            PlanCollection plans = Plan.list(planParams);
            log.info("PlanService getAllPlans: retrieved all plans: {}", plans.getData());
            return plans.getData();
        } catch (StripeException e) {
            log.error("PlanService getAllPlans: fail with {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Plan createPlan(PlanRequest planRequest) {
        try {
            log.info("PlanService addPlan: adding plan with: {}", planRequest);
            Map<String, Object> params = Map.of(
                    "currency", "USD",
                    "interval", planRequest.getInterval(),
                    "product", planRequest.getProductId(),
                    "nickname", planRequest.getNickname(),
                    "amount", planRequest.getNickname()
            );
            Plan plan = Plan.create(params);
            log.info("PlanService addPlan: add success: {}", plan);
            return plan;
        } catch (StripeException e) {
            log.error("PlanService createPlan: fail with {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deletePlan(String planId) {
        try {
            Plan plan = Plan.retrieve(planId);
            if (plan == null) {
                log.info("PlanService deletePlan: delete fail for planId: {}", planId);
                return;
            }
            plan.delete();
            log.info("PlanService deletePlan: delete success for planId: {}", planId);
        } catch (StripeException e) {
            log.error("PlanService deletePlan: fail with {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
