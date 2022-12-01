package com.example.nftmarket.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class AuctionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auctionUpdateId;

    private float offerPrice;
    private Timestamp offerTime;
    private Timestamp expirationTime;
    private boolean isSold;

    @ManyToOne
    private NFTTransactions nftTransactions;

    public AuctionHistory() {
    }

    public AuctionHistory(int auctionUpdateId, float offerPrice, Timestamp offerTime, Timestamp expirationTime, boolean isSold, NFTTransactions nftTransactions) {
        this.auctionUpdateId = auctionUpdateId;
        this.offerPrice = offerPrice;
        this.offerTime = offerTime;
        this.expirationTime = expirationTime;
        this.isSold = isSold;
        this.nftTransactions = nftTransactions;
    }

    public int getAuctionUpdateId() {
        return auctionUpdateId;
    }

    public void setAuctionUpdateId(int auctionUpdateId) {
        this.auctionUpdateId = auctionUpdateId;
    }

    public float getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(float offerPrice) {
        this.offerPrice = offerPrice;
    }

    public Timestamp getOfferTime() {
        return offerTime;
    }

    public void setOfferTime(Timestamp offerTime) {
        this.offerTime = offerTime;
    }

    public Timestamp getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Timestamp expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public NFTTransactions getNftTransactions() {
        return nftTransactions;
    }

    public void setNftTransactions(NFTTransactions nftTransactions) {
        this.nftTransactions = nftTransactions;
    }
}
