package com.example.nftmarket.Entity;

import javax.persistence.*;

@Entity
public class CryptoTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int cryptoTransactionId;

    private String transactionType;
    private float transactionAmount;
    private float previousBalance;
    private float updatedBalance;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cryptoTransactions", fetch = FetchType.LAZY)
    private NFTCryptoTransactions nftCryptoTransactions;

    @ManyToOne
    private CryptoCurrencies cryptoCurrencies;

    public CryptoTransactions(int cryptoTransactionId, String transactionType, float transactionAmount, float previousBalance, float updatedBalance, NFTCryptoTransactions nftCryptoTransactions, CryptoCurrencies cryptoCurrencies) {
        this.cryptoTransactionId = cryptoTransactionId;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.previousBalance = previousBalance;
        this.updatedBalance = updatedBalance;
        this.nftCryptoTransactions = nftCryptoTransactions;
        this.cryptoCurrencies = cryptoCurrencies;
    }

    public CryptoTransactions() {
    }

    public int getCryptoTransactionId() {
        return cryptoTransactionId;
    }

    public void setCryptoTransactionId(int cryptoTransactionId) {
        this.cryptoTransactionId = cryptoTransactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public float getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public float getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(float previousBalance) {
        this.previousBalance = previousBalance;
    }

    public float getUpdatedBalance() {
        return updatedBalance;
    }

    public void setUpdatedBalance(float updatedBalance) {
        this.updatedBalance = updatedBalance;
    }

    public NFTCryptoTransactions getNftCryptoTransactions() {
        return nftCryptoTransactions;
    }

    public void setNftCryptoTransactions(NFTCryptoTransactions nftCryptoTransactions) {
        this.nftCryptoTransactions = nftCryptoTransactions;
    }

    public CryptoCurrencies getCryptoCurrencies() {
        return cryptoCurrencies;
    }

    public void setCryptoCurrencies(CryptoCurrencies cryptoCurrencies) {
        this.cryptoCurrencies = cryptoCurrencies;
    }

}
