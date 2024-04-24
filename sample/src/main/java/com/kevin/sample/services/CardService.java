package com.kevin.sample.services;

import com.kevin.sample.dto.responses.CardTokenResponse;
import com.kevin.sample.dto.requests.ChargeRequest;
import com.kevin.sample.dto.requests.CardTokenRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CardService {
    @Value("${stripe.key}")
    private String stripeKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }

    public CardTokenResponse createCardToken(CardTokenRequest cardTokenRequest) {
        try {
            log.info("CardService chargeCard: creating card token with {}", cardTokenRequest);
            Map<String, Object> cardParams = Map.of(
                    "number", cardTokenRequest.getCardNumber(),
                    "exp_month", Integer.parseInt(cardTokenRequest.getExpMonth()),
                    "exp_year", Integer.parseInt(cardTokenRequest.getExpYear()),
                    "cvc", cardTokenRequest.getCvc()
            );
            Map<String, Object> tokenParams = Map.of("card", cardParams);
            Token token = Token.create(tokenParams);
            CardTokenResponse cardTokenResponse = null;

            if (token != null) {
                cardTokenResponse = CardTokenResponse.builder()
                        .token(token.getId())
                        .success(true)
                        .build();
            }

            return cardTokenResponse;
        } catch (StripeException e) {
            log.error("CardService createCardToken: fail with {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Charge chargeCard(ChargeRequest chargeRequest) {
        try {
            log.info("CardService chargeCard: charging card with {}", chargeRequest);
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("id", chargeRequest.getChargeId());
            metadata.putAll(chargeRequest.getAdditionalInfo());

            Map<String, Object> chargeParams = Map.of(
                    "amount", chargeRequest.getAmount(),
                    "currency", "USD",
                    "source", chargeRequest.getStripeToken(),
                    "metadata", metadata
            );

            Charge charge = Charge.create(chargeParams);
            log.info("CardService chargeCard: success {}", charge);
            return charge;
        } catch (StripeException e) {
            log.error("CardService chargeCard: fail with {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
