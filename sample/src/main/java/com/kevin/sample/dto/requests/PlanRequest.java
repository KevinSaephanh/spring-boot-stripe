package com.kevin.sample.dto.requests;

import lombok.Data;

@Data
public class PlanRequest {
    private long amount;
    private String interval;
    private String nickname;
    private String productId;
}
