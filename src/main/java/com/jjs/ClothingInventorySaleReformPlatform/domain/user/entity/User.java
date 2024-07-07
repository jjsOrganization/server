package com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
public class User implements UserDetails {  // 구매자

    @Id
    @Column(name = "EMAIL",nullable = false, unique = true)  // unique = true : 유일값(중복x)
    private String email;  // 이메일

    @Column(name = "PASSWORD", nullable = false)
    private String password;  // 비밀번호

    @Column(name = "NAME", nullable = false)
    private String name;  // 이름

    @Column(name = "PHONENUMBER", unique = true, nullable = false)
    private String phoneNumber;  // 전화번호

    @Column(name = "ROLE", nullable = false)
    private String role;  // 권한

    public User(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
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