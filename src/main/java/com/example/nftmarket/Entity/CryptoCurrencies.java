package com.example.nftmarket.Entity;

import javax.persistence.*;

@Entity
public class CryptoCurrencies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cryptoId;

    private String currencyType;
    private String balance;

    public CryptoCurrencies(int cryptoId, String currencyType, String balance) {
        this.cryptoId = cryptoId;
        this.currencyType = currencyType;
        this.balance = balance;
    }

    public CryptoCurrencies() {
    }

    public int getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(int cryptoId) {
        this.cryptoId = cryptoId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
