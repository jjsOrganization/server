package com.jjs.ClothingInventorySaleReformPlatform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.CustomUserDetails;
import com.jjs.ClothingInventorySaleReformPlatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // DB에서 조회
        User userData = userRepository.findByEmail(email);

        if (userData != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
