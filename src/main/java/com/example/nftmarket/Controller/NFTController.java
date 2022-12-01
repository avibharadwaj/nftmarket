package com.example.nftmarket.Controller;

import com.example.nftmarket.Service.NFTService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController()
@RequestMapping(value = "/nft")
public class NFTController {

    @Autowired
    private NFTService nftService;

    @GetMapping(value = "/viewAll")
    public ResponseEntity<?> getNfts() throws JSONException {
        return nftService.getNftsOfUser();
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> addNft(
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("description") String desc,
            @RequestParam("image_url") String imageUrl,
            @RequestParam("asset_url") String assetUrl
    ) {
        return nftService.addNft(name, type, desc, imageUrl, assetUrl);
    }

    @PostMapping(value = "/sell")
    public ResponseEntity<?> sellNft(
            @RequestParam("nftId") int nftId,
            @RequestParam("currencyType") String currencyType,
            @RequestParam("saleType") String saleType,
            @RequestParam("listPrice") float listPrice
    ) {
        return nftService.sellNft(nftId, currencyType, saleType, listPrice);
    }

    @GetMapping(value = "/sell/viewListings")
    public ResponseEntity<?> viewListedNfts() throws JSONException {
        return nftService.viewListedNfts();
    }

    @PostMapping(value = "/sell/cancelListing/{nftId}")
    public ResponseEntity<?> cancelListing(@PathVariable int nftId) {
        return nftService.cancelListing(nftId);
    }

    @GetMapping(value = "/buy/viewListings")
    public ResponseEntity<?> viewOnSaleNfts() throws JSONException {
        return nftService.viewOnSaleNfts();
    }

    @PostMapping(value = "/buy/pricedItem/{nftId}")
    public ResponseEntity<?> buyPricedItem(@PathVariable int nftId) {
        return nftService.buyPricedNFT(nftId);
    }

    @PostMapping(value = "/buy/makeOffer/auctionItem")
    public ResponseEntity<?> makeOfferAuctionItem(
            @RequestParam("nftId") int nftId,
            @RequestParam("offerPrice") float offerPrice,
            @RequestParam("expirationTime") Timestamp expirationTime
    ) {
        return nftService.makeOfferAuctionItem(nftId, offerPrice, expirationTime);
    }

    @PostMapping(value = "/buy/cancelOffer/auctionItem")
    public ResponseEntity<?> makeOfferAuctionItem(
            @RequestParam("nftId") int nftId
    ) {
        return nftService.cancelOfferAuctionItem(nftId);
    }

}
