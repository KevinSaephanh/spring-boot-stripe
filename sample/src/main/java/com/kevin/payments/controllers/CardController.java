package com.kevin.payments.controllers;

import com.kevin.payments.dao.requests.CardTokenRequest;
import com.kevin.payments.dao.responses.CardTokenResponse;
import com.kevin.payments.dao.requests.ChargeRequest;
import com.kevin.payments.services.CardService;
import com.stripe.model.Charge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@RequestMapping("v1/card")
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping("/create-token")
    @ResponseBody
    public CardTokenResponse createCardToken(CardTokenRequest cardTokenRequest) {
        log.info("CardController createCardToken: {}", cardTokenRequest);
        return cardService.createCardToken(cardTokenRequest);
    }

    @PostMapping("/charge")
    @ResponseBody
    public Charge chargeCard(@RequestBody ChargeRequest chargeRequest) {
        log.info("CardController chargeCard: {}", chargeRequest);
        return cardService.chargeCard(chargeRequest);
    }
}
