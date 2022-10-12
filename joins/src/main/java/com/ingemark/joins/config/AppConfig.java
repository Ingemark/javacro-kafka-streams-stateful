package com.ingemark.joins.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "com.ingemark.joins.kafka")
public class AppConfig {

    private String applicationId;
    private String bootstrapServers;
    private String purchaseSourceTopic;
    private String coffeePurchaseSourceTopic;
    private String promotionSinkTopic;
    private BigDecimal loyaltyLimit;

    private Integer promotionPoints;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getPurchaseSourceTopic() {
        return purchaseSourceTopic;
    }

    public void setPurchaseSourceTopic(String purchaseSourceTopic) {
        this.purchaseSourceTopic = purchaseSourceTopic;
    }

    public String getCoffeePurchaseSourceTopic() {
        return coffeePurchaseSourceTopic;
    }

    public void setCoffeePurchaseSourceTopic(String coffeePurchaseSourceTopic) {
        this.coffeePurchaseSourceTopic = coffeePurchaseSourceTopic;
    }

    public String getPromotionSinkTopic() {
        return promotionSinkTopic;
    }

    public void setPromotionSinkTopic(String promotionSinkTopic) {
        this.promotionSinkTopic = promotionSinkTopic;
    }

    public BigDecimal getLoyaltyLimit() {
        return loyaltyLimit;
    }

    public void setLoyaltyLimit(BigDecimal loyaltyLimit) {
        this.loyaltyLimit = loyaltyLimit;
    }

    public Integer getPromotionPoints() {
        return promotionPoints;
    }

    public void setPromotionPoints(Integer promotionPoints) {
        this.promotionPoints = promotionPoints;
    }
}
