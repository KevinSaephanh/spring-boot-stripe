package com.kevin.sample.dto.requests;

import lombok.Data;

@Data
public class CardTokenRequest {
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String username;
}