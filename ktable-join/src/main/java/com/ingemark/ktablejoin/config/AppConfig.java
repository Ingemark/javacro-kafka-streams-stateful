package com.ingemark.ktablejoin.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.ingemark.ktablejoin.kafka")
public class AppConfig {

    private String applicationId;
    private String bootstrapServers;
    private String customerInfoSourceTopic;
    private String paymentOrderSourceTopic;
    private String enrichedPaymentOrderSinkTopic;

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

    public String getCustomerInfoSourceTopic() {
        return customerInfoSourceTopic;
    }

    public void setCustomerInfoSourceTopic(String customerInfoSourceTopic) {
        this.customerInfoSourceTopic = customerInfoSourceTopic;
    }

    public String getPaymentOrderSourceTopic() {
        return paymentOrderSourceTopic;
    }

    public void setPaymentOrderSourceTopic(String paymentOrderSourceTopic) {
        this.paymentOrderSourceTopic = paymentOrderSourceTopic;
    }

    public String getEnrichedPaymentOrderSinkTopic() {
        return enrichedPaymentOrderSinkTopic;
    }

    public void setEnrichedPaymentOrderSinkTopic(String enrichedPaymentOrderSinkTopic) {
        this.enrichedPaymentOrderSinkTopic = enrichedPaymentOrderSinkTopic;
    }
}
