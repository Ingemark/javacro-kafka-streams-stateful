package com.ingemark.simpletransformer.transformer;


import com.ingemark.simpletransformer.config.AppConfig;
import com.ingemark.simpletransformer.pojo.Purchase;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PurchaseLoyaltyTransformer implements ValueTransformer<Purchase, Purchase> {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseLoyaltyTransformer.class);
    private KeyValueStore<String, Integer> purchaseLoyaltyStore;
    private final String loyaltyStoreName;
    private final AppConfig config;

    public PurchaseLoyaltyTransformer(String loyaltyStoreName, AppConfig config) {
        this.loyaltyStoreName = loyaltyStoreName;
        this.config = config;
    }

    @Override
    public void init(ProcessorContext context) {
        purchaseLoyaltyStore = context.getStateStore(loyaltyStoreName);
    }

    @Override
    public Purchase transform(Purchase purchase) {
        int totalLoyaltyPoints = 0;
        var oldTotalLoyaltyPoints = purchaseLoyaltyStore.get(purchase.getCustomerId());

        if(oldTotalLoyaltyPoints == null){
            oldTotalLoyaltyPoints = 0;
        }

        if (isPurchaseAboveLoyaltyLimit(purchase.getPurchaseTotal())) {

            var newLoyaltyPoints = calculateNewLoyaltyPoints(purchase.getPurchaseTotal());

            logger.info("newAcquiredLoyaltyPoints: {}, oldTotalLoyaltyPoints: {}" , newLoyaltyPoints, oldTotalLoyaltyPoints);

            if(oldTotalLoyaltyPoints == 0){
                totalLoyaltyPoints = newLoyaltyPoints;
            } else {
                totalLoyaltyPoints = newLoyaltyPoints + oldTotalLoyaltyPoints;
            }

            purchaseLoyaltyStore.put(purchase.getCustomerId(), totalLoyaltyPoints);

        } else {
            totalLoyaltyPoints = oldTotalLoyaltyPoints;
        }

        logger.info("newTotalLoyaltyPoints: {}", totalLoyaltyPoints);

        return Purchase.PurchaseBuilder
                .aPurchase(purchase)
                .withLoyaltyPoints(totalLoyaltyPoints)
                .build();
    }

    @Override
    public void close() {
        //nothing to do
    }

    private Integer calculateNewLoyaltyPoints(BigDecimal purchaseTotal) {
        var loyaltyLimit = config.getLoyaltyLimit();
        return purchaseTotal.divide(loyaltyLimit, RoundingMode.HALF_UP).intValue();
    }

    private boolean isPurchaseAboveLoyaltyLimit(BigDecimal purchase){
        var loyaltyLimit = config.getLoyaltyLimit();
        return purchase.compareTo(loyaltyLimit) >= 0;
    }


}
