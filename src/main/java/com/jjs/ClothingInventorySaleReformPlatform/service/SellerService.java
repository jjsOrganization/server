package com.jjs.ClothingInventorySaleReformPlatform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Purchaser;
import com.jjs.ClothingInventorySaleReformPlatform.domain.Seller;
import com.jjs.ClothingInventorySaleReformPlatform.dto.PurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.SellerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ToString
public class SellerService {

    private final SellerRepository sellerRepository;

    @Transactional
    public void save(SellerDTO sellerDTO) {
        Seller seller = Seller.toSeller(sellerDTO);
        validateDuplicateSellerEmail(seller); // 입력한 이메일이 이미 db에 존재하는지 검사
        validateDuplicateSellerPhoneNumber(seller); // 입력한 전화번호가 이미 db에 존재하는지 검사
        validateDuplicateSellerNickname(seller); // 입력한 닉네임이 이미 db에 존재하는지 검사
        sellerRepository.save(seller);
    }

    public String login(SellerDTO sellerDTO) {
        Optional<Seller> sellerByEmail = sellerRepository.findByEmail(sellerDTO.getEmail()); // 이메일로 DB에서 회원 조회
        if (sellerByEmail.isPresent()) {
            Seller seller = sellerByEmail.get(); // 로그인 된 사용자 정보
            if (seller.getPassword().equals(sellerDTO.getPassword())) {
                return SellerDTO.toSellerDTO(seller).getEmail(); // 로그인된 유저의 이메일만 넘김
            }
            else {
                return null;
            }
        }
        else{
            return null;
        }
    }

    private void validateDuplicateSellerEmail(Seller seller) {
        Optional<Seller> sellerByEmail = sellerRepository.findByEmail(seller.getEmail());
        if(sellerByEmail.isPresent()){
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    private void validateDuplicateSellerPhoneNumber(Seller seller) {
        Optional<Seller> sellerByPhoneNumber = sellerRepository.findByPhoneNumber(seller.getPhoneNumber());
        if(sellerByPhoneNumber.isPresent()){
            throw new IllegalStateException("이미 존재하는 전화번호입니다.");
        }
    }

    private void validateDuplicateSellerNickname(Seller seller) {
        Optional<Seller> sellerByNickName = sellerRepository.findByNickName(seller.getNickName());
        if(sellerByNickName.isPresent() ){
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }
}
