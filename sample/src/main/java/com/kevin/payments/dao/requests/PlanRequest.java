package com.kevin.payments.dao.requests;

import lombok.Data;

@Data
public class PlanRequest {
    private long amount;
    private String interval;
    private String nickname;
    private String productId;
}
