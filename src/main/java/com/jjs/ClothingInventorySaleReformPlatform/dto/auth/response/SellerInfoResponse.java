package com.jjs.ClothingInventorySaleReformPlatform.dto.auth.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.SellerInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellerInfoResponse {
    private String email;
    private String phoneNumber;
    private String storeName;
    private String storeAddress;

    public SellerInfoResponse(String email, String phoneNumber, String storeName, String storeAddress) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }

    public static SellerInfoResponse from(SellerInfo sellerInfo) {
        return new SellerInfoResponse(
                sellerInfo.getUser().getEmail(),
                sellerInfo.getUser().getPhoneNumber(),
                sellerInfo.getStoreName(),
                sellerInfo.getStoreAddress()
        );
    }
}
