package com.ingemark.ktablejoin.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EnrichedPaymentOrder {

    private String customerId;
    private String name;
    private String surname;
    private String accountType;
    private String debtorIban;
    private String creditorIban;
    private String merchantId;
    private String merchantName;
    private BigDecimal amount;
    private String currency;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDebtorIban() {
        return debtorIban;
    }

    public void setDebtorIban(String debtorIban) {
        this.debtorIban = debtorIban;
    }

    public String getCreditorIban() {
        return creditorIban;
    }

    public void setCreditorIban(String creditorIban) {
        this.creditorIban = creditorIban;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "EnrichedPaymentOrder{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", accountType='" + accountType + '\'' +
                ", debtorIban='" + debtorIban + '\'' +
                ", creditorIban='" + creditorIban + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }


    public static final class EnrichedPaymentOrderBuilder {
        private String customerId;
        private String name;
        private String surname;
        private String accountType;
        private String debtorIban;
        private String creditorIban;
        private String merchantId;
        private String merchantName;
        private BigDecimal amount;
        private String currency;

        private EnrichedPaymentOrderBuilder() {
        }

        public static EnrichedPaymentOrderBuilder anEnrichedPaymentOrder() {
            return new EnrichedPaymentOrderBuilder();
        }

        public static EnrichedPaymentOrderBuilder anEnrichedPaymentOrder(PaymentOrder paymentOrder){
            var builder = new EnrichedPaymentOrderBuilder();
            builder.customerId = paymentOrder.getCustomerId();
            builder.debtorIban = paymentOrder.getDebtorIban();
            builder.creditorIban = paymentOrder.getCreditorIban();
            builder.merchantId = paymentOrder.getMerchantId();
            builder.merchantName = paymentOrder.getMerchantName();
            builder.amount = paymentOrder.getAmount();
            builder.currency = paymentOrder.getCurrency();

            return builder;
        }

        public EnrichedPaymentOrderBuilder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public EnrichedPaymentOrderBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EnrichedPaymentOrderBuilder withSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public EnrichedPaymentOrderBuilder withAccountType(String accountType) {
            this.accountType = accountType;
            return this;
        }

        public EnrichedPaymentOrderBuilder withDebtorIban(String debtorIban) {
            this.debtorIban = debtorIban;
            return this;
        }

        public EnrichedPaymentOrderBuilder withCreditorIban(String creditorIban) {
            this.creditorIban = creditorIban;
            return this;
        }

        public EnrichedPaymentOrderBuilder withMerchantId(String merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public EnrichedPaymentOrderBuilder withMerchantName(String merchantName) {
            this.merchantName = merchantName;
            return this;
        }

        public EnrichedPaymentOrderBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public EnrichedPaymentOrderBuilder withCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public EnrichedPaymentOrder build() {
            EnrichedPaymentOrder enrichedPaymentOrder = new EnrichedPaymentOrder();
            enrichedPaymentOrder.setCustomerId(customerId);
            enrichedPaymentOrder.setName(name);
            enrichedPaymentOrder.setSurname(surname);
            enrichedPaymentOrder.setAccountType(accountType);
            enrichedPaymentOrder.setDebtorIban(debtorIban);
            enrichedPaymentOrder.setCreditorIban(creditorIban);
            enrichedPaymentOrder.setMerchantId(merchantId);
            enrichedPaymentOrder.setMerchantName(merchantName);
            enrichedPaymentOrder.setAmount(amount);
            enrichedPaymentOrder.setCurrency(currency);
            return enrichedPaymentOrder;
        }
    }
}
