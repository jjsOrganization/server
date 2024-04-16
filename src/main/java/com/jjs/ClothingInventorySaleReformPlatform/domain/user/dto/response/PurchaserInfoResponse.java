package com.jjs.ClothingInventorySaleReformPlatform.domain.user.dto.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaserInfoResponse {
    private String email;
    private String name;
    private String phoneNumber;
    private String address;

    public PurchaserInfoResponse(String email, String name, String phoneNumber, String address) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public static PurchaserInfoResponse from(PurchaserInfo purchaserInfo) {
        return new PurchaserInfoResponse(
                purchaserInfo.getUser().getEmail(),
                purchaserInfo.getUser().getName(),
                purchaserInfo.getUser().getPhoneNumber(),
                purchaserInfo.getAddress()
        );
    }
}
