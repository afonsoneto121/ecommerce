package com.hatanaka.ecommerce.payment.listener;

import br.com.ecommerce.checkout.event.CheckoutCreatedEvent;
import br.com.ecommerce.payment.event.PaymentCreatedEvent;
import com.hatanaka.ecommerce.payment.streaming.CheckoutProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;



@Component
public class CheckoutCreatedListener {

    private final CheckoutProcessor checkoutProcessor;

    public CheckoutCreatedListener(CheckoutProcessor checkoutProcessor) {
        this.checkoutProcessor = checkoutProcessor;
    }

    @StreamListener(CheckoutProcessor.INPUT)
    public void handle(CheckoutCreatedEvent event) {
        final PaymentCreatedEvent createdEvent = PaymentCreatedEvent.newBuilder()
                .setCheckoutCode(event.getCheckoutCode())
                .setCheckoutStatus(event.getStatus())
                .setPaymentCode(UUID.randomUUID().toString())
                .build();
        checkoutProcessor.output().send(MessageBuilder.withPayload(createdEvent).build());
    }
}
