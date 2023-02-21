package io.flowing.retail.payment.service;

import org.springframework.stereotype.Service;
import io.flowing.retail.payment.messages.payload.RetrievePaymentCommandPayload;
import lombok.extern.java.Log;

/**
 * PaymentService
 */
@Service
@Log
public class PaymentService {

    /**
     * Receive payment from customer
     * 
     * @param retrievePaymentCommand info about order
     * @throws InterruptedException
     */
    public void receive(RetrievePaymentCommandPayload retrievePaymentCommand) throws InterruptedException {
        log.info(String.format("Payment has been received. Order id: %s, reason: %s, amount: %s",
                retrievePaymentCommand.getRefId(),
                retrievePaymentCommand.getReason(),
                retrievePaymentCommand.getAmount()));
        
        Thread.sleep(60_000);
    }

}