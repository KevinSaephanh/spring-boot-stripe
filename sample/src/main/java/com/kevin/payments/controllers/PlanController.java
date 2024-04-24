package com.kevin.payments.controllers;

import com.kevin.payments.dao.requests.PlanRequest;
import com.kevin.payments.services.PlanService;
import com.stripe.model.Plan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("v1/plans")
public class PlanController {
    @Autowired
    private PlanService planService;

    @GetMapping()
    @ResponseBody
    public List<Plan> getAllPlans() throws Exception {
        log.info("PlanController: get all plans");
        return planService.getAllPlans();
    }

    @PostMapping("/add")
    @ResponseBody
    public Plan createPlan(@RequestBody PlanRequest planRequest) throws Exception {
        log.info("PlanController createPlan: with: {}", planRequest);
        return planService.createPlan(planRequest);
    }

    @DeleteMapping("/delete/{planId}")
    @ResponseBody
    public void deletePlan(@PathVariable String planId) throws Exception {
        log.info("PlanController deletePlan: with id: {}", planId);
        planService.deletePlan(planId);
    }
}
