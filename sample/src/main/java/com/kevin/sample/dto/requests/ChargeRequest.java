package com.kevin.sample.dto.requests;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ChargeRequest {
    private String  stripeToken;
    private String  username;
    private Double  amount;
    private String  message;
    private String chargeId;
    private Map<String,Object> additionalInfo = new HashMap<>();
}
