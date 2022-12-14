package com.example.nftmarket.UserDetails;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nftmarket.Entity.Users;

public class CustomUserDetails implements UserDetails {
	 
    private Users user;
     
    public CustomUserDetails(Users user) {
        this.user = user;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
 
    @Override
    public String getPassword() {
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getUsername();
    }
 
    public String getNickName() {
    	return user.getNickName();
    }
 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
    
}
