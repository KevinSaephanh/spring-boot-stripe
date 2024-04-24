package com.kevin.sample.dto.requests;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String email;
    private String priceId;
    private String planId;
    private String username;
    private long numberOfLicense;
    private boolean success;
}
