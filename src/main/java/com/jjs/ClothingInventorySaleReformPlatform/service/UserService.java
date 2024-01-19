package com.jjs.ClothingInventorySaleReformPlatform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.SellerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.DesignerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.PurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.SellerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.UserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.DesignerRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.SellerRepository;
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
    private final SellerRepository sellerRepository;
    private final DesignerRepository designerRepository;
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



        // PurchaserInfo 엔티티 생성 및 설정
        PurchaserInfo purchaserInfo = new PurchaserInfo();
        purchaserInfo.setAddress(address);
        purchaserInfo.setUser(user);  //   User 엔티티와 연결

        // 엔티티에 저장
        purchaserRepository.save(purchaserInfo);

    }

    public void joinSeller(SellerDTO sellerDTO) {
        String email = sellerDTO.getEmail();
        String password = sellerDTO.getPassword();
        String name = sellerDTO.getName();
        String phoneNumber = sellerDTO.getPhoneNumber();
        String storeName = sellerDTO.getStoreName();
        String storeAddress = sellerDTO.getStoreAddress();
        String businessNumber = sellerDTO.getBusinessNumber();

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
            newUser.setRole("ROLE_SELLER");
            return userRepository.save(newUser);
        });

        // SellerInfo 엔티티 생성 및 설정
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setStoreName(storeName);
        sellerInfo.setStoreAddress(storeAddress);
        sellerInfo.setBusinessNumber(businessNumber);
        sellerInfo.setUser(user);  //   User 엔티티와 연결

        // 엔티티에 저장
        sellerRepository.save(sellerInfo);
    }

    public void joinDesigner(DesignerDTO designerDTO) {
        String email = designerDTO.getEmail();
        String password = designerDTO.getPassword();
        String name = designerDTO.getName();
        String phoneNumber = designerDTO.getPhoneNumber();
        String address = designerDTO.getAddress();


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
            newUser.setRole("ROLE_DESIGNER");
            return userRepository.save(newUser);
        });

        // DesignerInfo 엔티티 생성 및 설정
        DesignerInfo designerInfo = new DesignerInfo();
        designerInfo.setAddress(address);
        designerInfo.setUser(user);  //   User 엔티티와 연결

        // 엔티티에 저장
        designerRepository.save(designerInfo);
    }


}
