package com.jjs.ClothingInventorySaleReformPlatform.dto;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Seller;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SellerDTO {

    @NotEmpty
    private String email;

    private String password;
    private String nickName;
    private String name;
    private String storeName;
    private String businessNumber;
    private String storeAddress;
    private String phoneNumber;

    public static SellerDTO toSellerDTO(Seller seller){
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setEmail(seller.getEmail());
        sellerDTO.setPassword(seller.getPassword());
        sellerDTO.setNickName(seller.getNickName());
        sellerDTO.setName(seller.getName());
        sellerDTO.setStoreName(seller.getStoreName());
        sellerDTO.setBusinessNumber(seller.getBusinessNumber());
        sellerDTO.setStoreAddress(seller.getStoreAddress());
        sellerDTO.setPhoneNumber(seller.getPhoneNumber());

        return sellerDTO;
    }
}
