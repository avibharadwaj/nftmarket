package com.example.nftmarket.Controller;

import com.example.nftmarket.Entity.Users;
import com.example.nftmarket.Repository.UsersRepo;
import com.example.nftmarket.Service.CryptoCurrencyService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/crypto")
public class CryptoCurrencyController {

    @Autowired
    private UsersRepo userRepo;
    @Autowired
    private CryptoCurrencyService cryptoCurrencyService;

    @GetMapping(value = "/viewAll")
    public ResponseEntity<?> getCryptos(Principal principal) throws JSONException {
        Users user = userRepo.findByEmail(principal.getName());
        return cryptoCurrencyService.getCryptosOfUser(user);
    }

    @PostMapping(value = "/deposit")
    public ResponseEntity<?> depositCrypto(
            Principal principal,
            @RequestParam("type") String type,
            @RequestParam("amount") float amount
    ) {
        Users user = userRepo.findByEmail(principal.getName());
        return cryptoCurrencyService.depositCrypto(type, amount, user);
    }

    @PostMapping(value = "/withdraw")
    public ResponseEntity<?> withdrawCrypto(
            Principal principal,
            @RequestParam("type") String type,
            @RequestParam("amount") float amount
    ) {
        Users user = userRepo.findByEmail(principal.getName());
        return cryptoCurrencyService.withdrawCrypto(type, amount, user);
    }
}
