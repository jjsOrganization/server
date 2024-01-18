package com.jjs.ClothingInventorySaleReformPlatform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.PurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.UserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final PurchaserRepository purchaserRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void joinPurchaser(PurchaserDTO purchaserDTO) {
        String email = purchaserDTO.getEmail();
        String password = purchaserDTO.getPassword();
        String name = purchaserDTO.getName();
        String phoneNumber = purchaserDTO.getPhoneNumber();
        String address = purchaserDTO.getAddress();

        Boolean isExist = userRepository.existsByEmail(email);

        if(isExist) {
            log.info("중복된 이메일 입니다.");
            return ;
        }

        // User 엔티티 조회 또는 생성
        User user = userRepository.findById(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(bCryptPasswordEncoder.encode(password));
            newUser.setName(name);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setRole("ROLE_PURCHASER");
            return userRepository.save(newUser);
        });

        /*
        // User 엔티티 생성 및 설정
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setRole("ROLE_PURCHASER");
        userRepository.save(user);

         */

        // PurchaserInfo 엔티티 생성 및 설정
        PurchaserInfo purchaserInfo = new PurchaserInfo();
        purchaserInfo.setAddress(address);
        purchaserInfo.setUser(user);  //   User 엔티티와 연결

        // 엔티티에 저장
        purchaserRepository.save(purchaserInfo);

    }
/*
    public void joinProcess(UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String name = userDTO.getName();
        String phoneNumber = userDTO.getPhoneNumber();

        Boolean isExist = userRepository.existsByEmail(email);

        if(isExist) {
            return;
        }

        User data = new User();

        data.setEmail(email);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setName(name);
        data.setPhoneNumber(phoneNumber);
        data.setRole("ROLE_USER");

        userRepository.save(data);
    }

 */

}
