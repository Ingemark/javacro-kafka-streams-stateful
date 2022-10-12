package com.ingemark.ktablejoin.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerInfo {

    private String customerId;
    private String name;
    private String surname;
    private String accountType;
    private String accountIban;

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

    public String getAccountIban() {
        return accountIban;
    }

    public void setAccountIban(String accountIban) {
        this.accountIban = accountIban;
    }

    @Override
    public String toString() {
        return "CustomerInfo{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", accountType='" + accountType + '\'' +
                ", accountIban='" + accountIban + '\'' +
                '}';
    }
}
