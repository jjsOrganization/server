package com.jjs.ClothingInventorySaleReformPlatform.dto;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Purchaser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomPurchaserDetails implements UserDetails {

    private final Purchaser purchaser;


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
    public String getPassword() {
        return purchaser.getPassword();
    }

    @Override
    public String getUsername() {
        return purchaser.getEmail();
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
