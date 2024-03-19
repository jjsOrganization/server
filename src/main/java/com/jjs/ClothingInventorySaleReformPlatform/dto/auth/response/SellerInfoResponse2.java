package com.jjs.ClothingInventorySaleReformPlatform.dto.auth.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.SellerInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellerInfoResponse2 {

    private String email;
    private String name;
    private String phoneNumber;
    private String storeName;
    private String storeAddress;
    private String businessNumber;

    public SellerInfoResponse2(String email, String name, String phoneNumber, String storeName, String storeAddress, String businessNumber) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.businessNumber = businessNumber;
    }

    public static SellerInfoResponse2 from(SellerInfo sellerInfo) {
        return new SellerInfoResponse2(
                sellerInfo.getUser().getEmail(),
                sellerInfo.getUser().getName(),
                sellerInfo.getUser().getPhoneNumber(),
                sellerInfo.getStoreName(),
                sellerInfo.getStoreAddress(),
                sellerInfo.getBusinessNumber()
        );
    }
}
