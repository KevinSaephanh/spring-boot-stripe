package com.kevin.payments.dao.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionResponse {
    private String customerId;
    private String subscriptionId;
    private String paymentMethodId;
    private String username;
}
