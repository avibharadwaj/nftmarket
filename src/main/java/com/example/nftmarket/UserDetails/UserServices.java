package com.example.nftmarket.UserDetails;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.nftmarket.Entity.Users;
import com.example.nftmarket.Repository.UsersRepo;

import net.bytebuddy.utility.RandomString;


@Service
public class UserServices {
    @Autowired
    private UsersRepo repo;
     
    @Autowired
    private PasswordEncoder passwordEncoder;
     
    @Autowired
    private JavaMailSender mailSender;
 
     
    public void register(Users user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
         
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
         
        repo.save(user);
        System.out.println("Inside register"); 
        sendVerificationEmail(user, siteURL);     
    }
     
    private void sendVerificationEmail(Users user, String siteURL) throws MessagingException, UnsupportedEncodingException {
    	   String toAddress = user.getUsername();
    	    String fromAddress = "nftmarketplace773@gmail.com";
    	    String senderName = "NTM";
    	    String subject = "Please verify your registration";
    	    String content = "Dear [[name]],<br>"
    	            + "Please click the link below to verify your registration:<br>"
    	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
    	            + "Thank you,<br>"
    	            + "NTM";
    	     
    	    MimeMessage message = mailSender.createMimeMessage();
    	    MimeMessageHelper helper = new MimeMessageHelper(message);
    	     
    	    helper.setFrom(fromAddress, senderName);
    	    helper.setTo(toAddress);
    	    helper.setSubject(subject);
    	     
    	    content = content.replace("[[name]]", user.getUsername());
    	    String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
    	     
    	    content = content.replace("[[URL]]", verifyURL);
    	     
    	    helper.setText(content, true);
    	    System.out.println("About to send verification mail"); 
    	    mailSender.send(message);
    	    System.out.println("Sent message");
    }

    public boolean verify(String verificationCode) {
        Users user = repo.findByVerificationCode(verificationCode);
        System.out.println("Verifying User in UserServices"); 
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            repo.save(user);
             
            return true;
        }
         
    }
}
