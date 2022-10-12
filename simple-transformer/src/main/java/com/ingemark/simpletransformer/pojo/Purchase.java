package com.ingemark.simpletransformer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Purchase {

    private String customerId;
    private BigDecimal purchaseTotal;
    private int loyaltyPoints;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getPurchaseTotal() {
        return purchaseTotal;
    }

    public void setPurchaseTotal(BigDecimal purchaseTotal) {
        this.purchaseTotal = purchaseTotal;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "customerId='" + customerId + '\'' +
                ", purchaseTotal=" + purchaseTotal +
                ", loyaltyPoints=" + loyaltyPoints +
                '}';
    }


    public static final class PurchaseBuilder {
        private String customerId;
        private BigDecimal purchaseTotal;
        private int loyaltyPoints;

        private PurchaseBuilder() {
        }

        public static PurchaseBuilder aPurchase() {
            return new PurchaseBuilder();
        }

        public static PurchaseBuilder aPurchase(Purchase enrichedPurchase) {
            var builder = new PurchaseBuilder();
            builder.customerId = enrichedPurchase.customerId;
            builder.purchaseTotal = enrichedPurchase.purchaseTotal;
            return builder;
        }

        public PurchaseBuilder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public PurchaseBuilder withPurchaseTotal(BigDecimal purchaseTotal) {
            this.purchaseTotal = purchaseTotal;
            return this;
        }

        public PurchaseBuilder withLoyaltyPoints(int loyaltyPoints) {
            this.loyaltyPoints = loyaltyPoints;
            return this;
        }

        public Purchase build() {
            Purchase purchase = new Purchase();
            purchase.setCustomerId(customerId);
            purchase.setPurchaseTotal(purchaseTotal);
            purchase.setLoyaltyPoints(loyaltyPoints);
            return purchase;
        }
    }
}
