package com.jjs.ClothingInventorySaleReformPlatform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Purchaser;
import com.jjs.ClothingInventorySaleReformPlatform.dto.CustomPurchaserDetails;
import com.jjs.ClothingInventorySaleReformPlatform.repository.PurchaserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PurchaserRepository purchaserRepository;

    public CustomUserDetailsService(PurchaserRepository purchaserRepository) {
        this.purchaserRepository = purchaserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // DB에서 조회
        Purchaser purchaserData = purchaserRepository.findByEmail(email);

        if (purchaserData != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomPurchaserDetails(purchaserData);
        }

        return null;
    }
}
