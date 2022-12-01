package com.example.nftmarket.Service;

import com.example.nftmarket.Entity.*;
import com.example.nftmarket.Repository.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class NFTService {

    @Autowired
    private NFTRepo nftRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private NFTTransactionsRepo nftTransactionsRepo;

    @Autowired
    private CryptoCurrenciesRepo cryptoCurrenciesRepo;

    @Autowired
    private PricedSaleHistoryRepo pricedSaleHistoryRepo;

    @Autowired
    private AuctionHistoryRepo auctionHistoryRepo;

    @Autowired
    private CryptoTransactionsRepo cryptoTransactionsRepo;

    @Autowired
    private NFTCryptoTransactionsRepo nftCryptoTransactionsRepo;

    public ResponseEntity<?> getNftsOfUser() throws JSONException {
        int user_id = 1;
        Users user = usersRepo.findById(user_id).get();
        Wallet wallet =  user.getWallet();
        List<NFT> nfts = wallet.getNftList();

        if (nfts.size() == 0) {
            return new ResponseEntity<>("No nfts found", HttpStatus.BAD_REQUEST);
        }

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (NFT nft : nfts) {
            JSONObject entity = new JSONObject();
            entity.put("nftId", nft.getNftId());
            entity.put("name", nft.getName());
            entity.put("type", nft.getType());
            entity.put("description", nft.getDescription());
            entity.put("tokenId", nft.getTokenId());
            entity.put("smartContactAddress", nft.getSmartContactAddress());
            entity.put("imageUrl", nft.getImageUrl());
            entity.put("assetUrl", nft.getAssetUrl());
            entity.put("lastRecordTime", nft.getLastRecordTime());
            entities.add(entity);
        }

        return new ResponseEntity<>(entities.toString(), HttpStatus.OK);
    }

    public ResponseEntity<?> addNft(String name, String type, String desc, String imageUrl, String assetUrl) {

        try {
            Timestamp currTime = new Timestamp(System.currentTimeMillis());
            String tokenId = name + currTime;
            String sca = tokenId + "address";
            NFT nft = new NFT(tokenId, sca, name, type, desc, imageUrl, assetUrl, currTime, false, null);

            int user_id = 1;
            Users user = usersRepo.findById(user_id).get();
            System.out.println(user.getUsername());
            Wallet wallet = user.getWallet();

            nft.setWallet(wallet);
            nftRepo.save(nft);

            List<NFT> nftList = wallet.getNftList();
            nftList.add(nft);
            wallet.setNftList(nftList);
            walletRepo.save(wallet);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("NFT creation failed", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("NFT created: " + name, HttpStatus.CREATED);

    }

    public ResponseEntity<?> sellNft(int nftId, String currencyType, String saleType, float listPrice) {

        try {
            NFT nft = nftRepo.findById(nftId).get();
            List<NFTTransactions> nftTransactionsList = nft.getNftTransactionsList();

            List<NFTTransactions> openOrInProgressTransactions = nftTransactionsList.
                    stream().
                    filter(nftTransactions -> nftTransactions.getTransactionStatus().equalsIgnoreCase("open")
                            || nftTransactions.getTransactionStatus().equalsIgnoreCase("inProgress")).
                    toList();

            if (openOrInProgressTransactions.size() == 0) {
                nft.setListedForSale(true);
                nft.setListingType(saleType);
                nft.setListPrice(listPrice);
                nft.setCurrencyType(currencyType);


                NFTTransactions nftTransactions = new NFTTransactions(currencyType, listPrice, 0, "open", null);
                nftTransactions.setNft(nft);
                nftTransactionsList.add(nftTransactions);
                //nft.setNftTransactionsList(nftTransactionsList);

                nftRepo.save(nft);
                //nftTransactionsRepo.save(nftTransactions);
            } else {
                return new ResponseEntity<>("Given NFT already listed for sale", HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Operation failed", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("NFT listed for sale: " + nftRepo.findById(nftId).get().getName(), HttpStatus.OK);
    }

    public ResponseEntity<?> viewListedNfts() throws JSONException {

        int user_id = 1;
        Users user = usersRepo.findById(user_id).get();
        Wallet wallet =  user.getWallet();
        List<NFT> nfts = wallet.getNftList();

        List<NFT> listedNfts = nfts.stream().filter(NFT::isListedForSale).toList();

        if (listedNfts.size() == 0) {
            return new ResponseEntity<>("No nfts listed for sale", HttpStatus.BAD_REQUEST);
        }

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (NFT nft : listedNfts) {
            JSONObject entity = new JSONObject();
            entity.put("nftId", nft.getNftId());
            entity.put("name", nft.getName());
            entity.put("type", nft.getType());
            entity.put("description", nft.getDescription());
            entity.put("tokenId", nft.getTokenId());
            entity.put("smartContactAddress", nft.getSmartContactAddress());
            entity.put("imageUrl", nft.getImageUrl());
            entity.put("assetUrl", nft.getAssetUrl());
            entity.put("lastRecordTime", nft.getLastRecordTime());
            entity.put("isListedForSale", nft.isListedForSale());
            entity.put("saleType", nft.getListingType());
            entity.put("listPrice",nft.getListPrice());
            entity.put("currencyType", nft.getCurrencyType());
            entities.add(entity);
        }

        return new ResponseEntity<>(entities.toString(), HttpStatus.OK);
    }

    public ResponseEntity<?> cancelListing(int nftId) {
        try {
            NFT nft = nftRepo.findById(nftId).get();
            NFTTransactions openTransaction = getOpenTransaction(nft);
            openTransaction.setTransactionStatus("closed");
            nft.setListedForSale(false);
            nftTransactionsRepo.save(openTransaction);
            nftRepo.save(nft);
            return new ResponseEntity<>("Listing cancelled : " + nft.getName(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Operation failed", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> viewOnSaleNfts() {
        try {
            List<NFT> nftList = (List<NFT>) nftRepo.findAll();

            int userId = 1;
            List<NFT> listedForSaleNfts = nftList.stream().
                    filter(nft -> nft.isListedForSale() && nft.getWallet().getWalletId() != usersRepo.findById(userId).get().getWallet().getWalletId()).
                    toList();
            List<NFT> unSoldNfts = new ArrayList<>();

            for (NFT nft : listedForSaleNfts) {
                List<NFTTransactions> nftTransactions = nft.getNftTransactionsList();
                List<NFTTransactions> openTransactions = nftTransactions.stream().filter(
                        nftTransactions1 -> nftTransactions1.getTransactionStatus().equalsIgnoreCase("open")
                ).toList();
                if (openTransactions.size() > 0) {
                    unSoldNfts.add(nft);
                }
            }

            if (unSoldNfts.size() == 0) {
                return new ResponseEntity<>("No NFTs available to buy", HttpStatus.OK);
            }

            List<JSONObject> entities = new ArrayList<>();
            for (NFT nft : unSoldNfts) {
                JSONObject entity = new JSONObject();
                entity.put("nftId", nft.getNftId());
                entity.put("name", nft.getName());
                entity.put("type", nft.getType());
                entity.put("description", nft.getDescription());
                entity.put("imageUrl", nft.getImageUrl());
                entity.put("lastRecordTime", nft.getLastRecordTime());
                entity.put("saleType", nft.getListingType());
                if (nft.getListingType().equalsIgnoreCase("auction")) {
                    NFTTransactions nftTransactions = nft.getNftTransactionsList().stream().
                            max(Comparator.comparing(NFTTransactions::getCurrentMaxBid)).
                            orElse(new NFTTransactions());
                    float maxBid = 0;
                    if (nftTransactions.getNft() == null) {
                        maxBid = 0;
                    } else {
                        maxBid = nftTransactions.getCurrentMaxBid();
                    }
                    entity.put("highestOffer", maxBid);
                }
                entity.put("listPrice",nft.getListPrice());
                entity.put("currencyType", nft.getCurrencyType());
                entities.add(entity);
            }

            return new ResponseEntity<>(entities.toString(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Operation failed", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> buyPricedNFT(int nftId) {
        try {
            int user_id = 2;
            Users user = usersRepo.findById(user_id).get();
            Wallet wallet = user.getWallet();
            List<CryptoCurrencies> cryptoCurrencies = wallet.getCryptoCurrenciesList();

            NFT nft = nftRepo.findById(nftId).get();
            Optional<CryptoCurrencies> currency = cryptoCurrencies.stream().
                    filter(cryptoCurrencies1 -> cryptoCurrencies1.getCurrencyType().equalsIgnoreCase(nft.getCurrencyType())).
                    findFirst();

            if (currency.isPresent()) {
                CryptoCurrencies curr = currency.get();
                if (curr.getBalance() < nft.getListPrice()) {
                    return new ResponseEntity<>("Balance too low to buy. NFT price: " + nft.getListPrice() + ". Available balance: " + curr.getBalance(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("User " + user.getUsername() + " doesn't have the accepted crypto currency in his wallet to buy this NFT: " + nft.getName(), HttpStatus.BAD_REQUEST);
            }

            // update currencies and wallets
            CryptoCurrencies curr = currency.get();
            float prevBalance = curr.getBalance();
            float currBalance = prevBalance - nft.getListPrice();
            curr.setBalance(currBalance);

            Wallet prevWallet = nft.getWallet();
            nft.setWallet(wallet);
            prevWallet.getNftList().remove(nft);

            CryptoCurrencies prevCurr = prevWallet.getCryptoCurrenciesList().stream().
                    filter(cryptoCurrencies1 -> cryptoCurrencies1.getCurrencyType().equalsIgnoreCase(curr.getCurrencyType())).
                    findFirst().
                    orElse(new CryptoCurrencies(curr.getCurrencyType(), 0));
            prevCurr.setBalance(prevCurr.getBalance() + nft.getListPrice());

            nft.setSmartContactAddress(nft.getSmartContactAddress() + user.getUserId());
            nft.setLastRecordTime(new Timestamp(System.currentTimeMillis()));

            // update transactions
            NFTTransactions openTransaction = getOpenTransaction(nft);
            openTransaction.setTransactionStatus("closed");
            openTransaction.setPricedSaleHistory(
                    new PricedSaleHistory(new Timestamp(System.currentTimeMillis()), prevWallet.getWalletId(), wallet.getWalletId(), nft.getListPrice())
            );

            CryptoTransactions cryptoTransactions = new CryptoTransactions(
                nft.getCurrencyType(), nft.getListPrice(), prevBalance, currBalance, curr
            );

            NFTCryptoTransactions nftCryptoTransactions = new NFTCryptoTransactions(
                    nft.getListPrice(), prevWallet.getWalletId(), wallet.getWalletId(), new Timestamp(System.currentTimeMillis()), openTransaction, cryptoTransactions
            );

            cryptoTransactionsRepo.save(cryptoTransactions);
            nftCryptoTransactionsRepo.save(nftCryptoTransactions);

            return new ResponseEntity<>("NFT " + nft.getName() + " is sold to user " + user.getUsername() + ". Sold by: " + prevWallet.getUser().getUsername(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Operation Failed", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> makeOfferAuctionItem(int nftId, float offerPrice, Timestamp expirationTime) {

        int user_id = 2;
        Users user = usersRepo.findById(user_id).get();
        Wallet wallet = user.getWallet();
        List<CryptoCurrencies> cryptoCurrencies = wallet.getCryptoCurrenciesList();

        NFT nft = nftRepo.findById(nftId).get();
        Optional<CryptoCurrencies> currency = cryptoCurrencies.stream().
                filter(cryptoCurrencies1 -> cryptoCurrencies1.getCurrencyType().equalsIgnoreCase(nft.getCurrencyType())).
                findFirst();

        if (currency.isPresent()) {
            CryptoCurrencies curr = currency.get();
            NFTTransactions openTransaction = nft.getNftTransactionsList().stream().
                    filter(nftTransactions1 -> !nftTransactions1.getTransactionStatus().equalsIgnoreCase("closed")).findFirst().get();

            // offer lower than current maximum bid
            if (openTransaction.getCurrentMaxBid() >= offerPrice) {
                return new ResponseEntity<>("Existing offer in place with higher or equal bid price. Existing maximum offer: " + openTransaction.getCurrentMaxBid(), HttpStatus.BAD_REQUEST);
            }
            // balance low to make offer
            if (curr.getBalance() < offerPrice) {
                return new ResponseEntity<>("Balance too low to make offer. Available balance: " + curr.getBalance() + ". Current max offer: " + openTransaction.getCurrentMaxBid(), HttpStatus.BAD_REQUEST);
            }
        } else {
            // accepted currency not found
            return new ResponseEntity<>("User " + user.getUsername() + " doesn't have the accepted crypto currency in his wallet to make offer this NFT: " + nft.getName(), HttpStatus.BAD_REQUEST);
        }


        // TO DO
        /**
         * Update auction history wallet - set expiration time etc.,
         * Update NFTTransaction with the data accordingly
         * To keep track of currency balance for multiple offers - need to update wallet with reduced price, also update prev users wallet with new balance
         * If existing max offer is by the same user, update it or add new record?
         *
         */

        return new ResponseEntity<>("Operation Failed", HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<?> cancelOfferAuctionItem(int nftId) {

        /**
         * Check if curr the highest offer is by the same user, else proceed
         * Update auction table, add new param "auction_status" to have the status of offer tracked
         *
         */

        return new ResponseEntity<>("Operation Failed", HttpStatus.BAD_REQUEST);
    }

    private static NFTTransactions getOpenTransaction(NFT nft) {
        List<NFTTransactions> nftTransactionsList = nft.getNftTransactionsList();
        return nftTransactionsList.
                stream().
                filter(nftTransactions -> nftTransactions.getTransactionStatus().equalsIgnoreCase("open")).
                findFirst().
                get();
    }
}
