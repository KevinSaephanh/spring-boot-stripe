package com.kevin.payments.dto.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardTokenResponse {
    private boolean success;
    private String token;
}
