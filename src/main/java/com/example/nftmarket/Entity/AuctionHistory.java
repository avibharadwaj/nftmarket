package com.example.nftmarket.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class AuctionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auctionUpdateId;

    private float soldPrice;
    private Timestamp soldTime;

    @ManyToOne
    private NFTTransactions nftTransactions;

    public AuctionHistory() {
    }

    public AuctionHistory(int auctionUpdateId, float soldPrice, Timestamp soldTime, NFTTransactions nftTransactions) {
        this.auctionUpdateId = auctionUpdateId;
        this.soldPrice = soldPrice;
        this.soldTime = soldTime;
        this.nftTransactions = nftTransactions;
    }

    public int getAuctionUpdateId() {
        return auctionUpdateId;
    }

    public void setAuctionUpdateId(int auctionUpdateId) {
        this.auctionUpdateId = auctionUpdateId;
    }

    public float getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(float soldPrice) {
        this.soldPrice = soldPrice;
    }

    public Timestamp getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(Timestamp soldTime) {
        this.soldTime = soldTime;
    }

    public NFTTransactions getNftTransactions() {
        return nftTransactions;
    }

    public void setNftTransactions(NFTTransactions nftTransactions) {
        this.nftTransactions = nftTransactions;
    }
}
