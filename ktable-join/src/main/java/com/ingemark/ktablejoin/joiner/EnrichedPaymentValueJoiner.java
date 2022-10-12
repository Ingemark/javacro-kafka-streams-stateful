package com.ingemark.ktablejoin.joiner;

import com.ingemark.ktablejoin.pojo.CustomerInfo;
import com.ingemark.ktablejoin.pojo.EnrichedPaymentOrder;
import com.ingemark.ktablejoin.pojo.PaymentOrder;
import org.apache.kafka.streams.kstream.ValueJoiner;

public class EnrichedPaymentValueJoiner implements ValueJoiner<PaymentOrder, CustomerInfo, EnrichedPaymentOrder> {


    @Override
    public EnrichedPaymentOrder apply(PaymentOrder paymentOrder, CustomerInfo customerInfo) {

        return EnrichedPaymentOrder.EnrichedPaymentOrderBuilder.anEnrichedPaymentOrder(paymentOrder)
                .withName(customerInfo.getName())
                .withSurname(customerInfo.getSurname())
                .withAccountType(customerInfo.getAccountType())
                .build();
    }
}
