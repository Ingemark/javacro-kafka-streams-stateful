package com.ingemark.joins.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Promotion {

    private String customerId;
    private int loyaltyPoints;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "customerId='" + customerId + '\'' +
                ", loyaltyPoints=" + loyaltyPoints +
                '}';
    }


    public static final class PromotionBuilder {
        private String customerId;
        private int loyaltyPoints;

        private PromotionBuilder() {
        }

        public static PromotionBuilder aPromotion() {
            return new PromotionBuilder();
        }

        public PromotionBuilder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public PromotionBuilder withLoyaltyPoints(int loyaltyPoints) {
            this.loyaltyPoints = loyaltyPoints;
            return this;
        }

        public Promotion build() {
            Promotion promotion = new Promotion();
            promotion.setCustomerId(customerId);
            promotion.setLoyaltyPoints(loyaltyPoints);
            return promotion;
        }
    }
}
