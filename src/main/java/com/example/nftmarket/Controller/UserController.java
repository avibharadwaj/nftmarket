package com.example.nftmarket.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nftmarket.Entity.Users;
import com.example.nftmarket.Repository.UsersRepo;
import com.example.nftmarket.UserDetails.UserServices;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class UserController {
	
	@Autowired
	private UsersRepo userRepo;
	
    @Autowired
    private UserServices service;
	
    @GetMapping(value = "/user")
    public String welcome() {
        return "Index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
    	System.out.println("The user on register page is:");
    	System.out.println(model);
        model.addAttribute("user", new Users());
         
        return "SignUp";
    }

    @PostMapping("/process_register")
    public String processRegister(Users user, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
    	System.out.println("Inside Process Register");
    	System.out.println(user);
    	System.out.println(request);
        service.register(user, getSiteURL(request));       
        return "SignUpSuccess";
    }
     
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    } 
//    @PostMapping("/process_register")
//    public String processRegister(Users user) {
//    	System.out.println("The user after registration is:");
//    	System.out.println(user.getUsername());
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
//    	System.out.println(user.getPassword());
//    	System.out.println("What is the user repo?");
//    	System.out.println(userRepo);
//    	
//        userRepo.save(user);
//         
//        return "SignUpSuccess";
//    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<Users> listUsers = (List<Users>) userRepo.findAll();
        model.addAttribute("listUsers", listUsers);
         
        return "Homepage";
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
    	System.out.println("---Verifying code---");
        if (service.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
    
}
