package com.example.nftmarket.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Timer;

@Entity
public class NFTTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int nftTransactionId;

    private String cryptoType;
    private float listedPrice;
    private float currentMaxBid;
    private String transactionStatus;
    private Timestamp expirationTime;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "nftTransactions", fetch = FetchType.LAZY)
    private NFTCryptoTransactions nftCryptoTransactions;

    @ManyToOne
    private NFT nft;

    @OneToMany(targetEntity = AuctionHistory.class, cascade=CascadeType.ALL)
    private List<AuctionHistory> auctionHistories;


}
