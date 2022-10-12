package com.ingemark.joins.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopPurchase {

    private String customerId;
    private BigDecimal purchaseTotal;

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

    @Override
    public String toString() {
        return "ShopPurchase{" +
                "customerId='" + customerId + '\'' +
                ", purchaseTotal=" + purchaseTotal +
                '}';
    }
}
