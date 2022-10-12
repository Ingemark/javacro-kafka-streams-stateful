package com.ingemark.joins.joiner;

import com.ingemark.joins.config.AppConfig;
import com.ingemark.joins.pojo.CoffeePurchase;
import com.ingemark.joins.pojo.Promotion;
import com.ingemark.joins.pojo.ShopPurchase;
import org.apache.kafka.streams.kstream.ValueJoiner;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PromotionJoiner implements ValueJoiner<ShopPurchase, CoffeePurchase, Promotion> {

    private final AppConfig config;

    public PromotionJoiner(AppConfig config){
        this.config = config;
    }

    @Override
    public Promotion apply(ShopPurchase shop, CoffeePurchase coffee) {

        var spentTotal = shop.getPurchaseTotal().add(coffee.getPurchaseTotal());

        var currentLoyaltyPoints = calculateCurrentLoyaltyPoints(spentTotal);

        var loyaltyPointsWithPromotion = currentLoyaltyPoints + config.getPromotionPoints();

        return Promotion.PromotionBuilder.aPromotion()
                .withCustomerId(shop.getCustomerId())
                .withLoyaltyPoints(loyaltyPointsWithPromotion)
                .build();
    }


    private Integer calculateCurrentLoyaltyPoints(BigDecimal purchaseTotal) {
        var loyaltyLimit = config.getLoyaltyLimit();
        return purchaseTotal.divide(loyaltyLimit, RoundingMode.HALF_UP).intValue();
    }
}
