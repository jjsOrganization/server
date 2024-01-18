package com.jjs.ClothingInventorySaleReformPlatform.dto;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Purchaser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomPurchaserDetails implements UserDetails {

    private final Purchaser purchaser;

    public CustomPurchaserDetails(Purchaser purchaser) {
        this.purchaser = purchaser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return purchaser.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {  // password
        return purchaser.getPassword();
    }

    @Override
    public String getUsername() {  // email
        return purchaser.getEmail();
    }

    public String getName() {
        return purchaser.getName();
    }

    public String getNickname() {
        return purchaser.getNickname();
    }

    public String getAddress() {
        return purchaser.getAddress();
    }

    public String getPhoneNumber() {
        return purchaser.getPhoneNumber();
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
