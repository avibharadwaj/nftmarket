package com.example.nftmarket.Controller;

import com.example.nftmarket.Entity.Users;
import com.example.nftmarket.Repository.UsersRepo;
import com.example.nftmarket.Service.NFTService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController()
@RequestMapping(value = "/nft")
public class NFTController {

    @Autowired
    private NFTService nftService;

    @Autowired
    private UsersRepo userRepo;

    @GetMapping(value = "/viewAll")
    public ResponseEntity<?> getNfts(Principal principal) throws JSONException {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.getNftsOfUser(user);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> addNft(
            Principal principal,
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("description") String desc,
            @RequestParam("image_url") String imageUrl,
            @RequestParam("asset_url") String assetUrl
    ) {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.addNft(name, type, desc, imageUrl, assetUrl, user);
    }

    @PostMapping(value = "/sell")
    public ResponseEntity<?> sellNft(
            Principal principal,
            @RequestParam("nftId") int nftId,
            @RequestParam("currencyType") String currencyType,
            @RequestParam("saleType") String saleType,
            @RequestParam("listPrice") float listPrice
    ) {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.sellNft(nftId, currencyType, saleType, listPrice, user);
    }

    @GetMapping(value = "/sell/viewListings")
    public ResponseEntity<?> viewListedNfts(Principal principal) throws JSONException {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.viewListedNfts(user);
    }

    @PostMapping(value = "/sell/cancelListing/{nftId}")
    public ResponseEntity<?> cancelListing(@PathVariable int nftId, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.cancelListing(nftId, user);
    }

    @GetMapping(value = "/buy/viewListings")
    public ResponseEntity<?> viewOnSaleNfts(Principal principal) throws JSONException {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.viewOnSaleNfts(user);
    }

    @PostMapping(value = "/buy/pricedItem/{nftId}")
    public ResponseEntity<?> buyPricedItem(@PathVariable int nftId, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.buyPricedNFT(nftId, user);
    }

    @PostMapping(value = "/buy/makeOffer/auctionItem")
    public ResponseEntity<?> makeOfferAuctionItem(
            Principal principal,
            @RequestParam("nftId") int nftId,
            @RequestParam("offerPrice") float offerPrice,
            @RequestParam("expirationSeconds") int expirationSeconds
    ) {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.makeOfferAuctionItem(nftId, offerPrice, expirationSeconds, user);
    }

    @PostMapping(value = "/buy/cancelOffer/auctionItem")
    public ResponseEntity<?> cancelOfferAuctionItem(
            Principal principal,
            @RequestParam("nftId") int nftId,
            @RequestParam("auctionBidId") int auctionBidId
    ) {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.cancelOfferAuctionItem(nftId, auctionBidId, user);
    }

    @GetMapping(value = "/sell/viewOffers")
    public ResponseEntity<?> viewOffersReceived(Principal principal) throws JSONException {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.viewReceivedOffersForAuctionItem(user);
    }

    @GetMapping(value = "/buy/viewOffers")
    public ResponseEntity<?> viewOffersMade(Principal principal) throws JSONException {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.viewOffersMadeForAuctionItem(user);
    }

    @PostMapping(value = "/sell/acceptOffer/")
    public ResponseEntity<?> acceptOfferAuctionItem(
            Principal principal,
            @RequestParam("nftId") int nftId,
            @RequestParam("auctionBidId") int auctionBidId
    ) {
        Users user = userRepo.findByEmail(principal.getName());
        return nftService.acceptOfferAuctionItem(nftId, auctionBidId, user);
    }
}
