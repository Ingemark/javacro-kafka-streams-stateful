package com.ingemark.simpletransformer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "com.ingemark.simpletransformer.kafka")
public class AppConfig {

    private String applicationId;
    private String bootstrapServers;
    private String purchaseSourceTopic;
    private String enrichedPurchaseSinkTopic;
    private BigDecimal loyaltyLimit;

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

    public String getEnrichedPurchaseSinkTopic() {
        return enrichedPurchaseSinkTopic;
    }

    public void setEnrichedPurchaseSinkTopic(String enrichedPurchaseSinkTopic) {
        this.enrichedPurchaseSinkTopic = enrichedPurchaseSinkTopic;
    }

    public BigDecimal getLoyaltyLimit() {
        return loyaltyLimit;
    }

    public void setLoyaltyLimit(BigDecimal loyaltyLimit) {
        this.loyaltyLimit = loyaltyLimit;
    }
}
