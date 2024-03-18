package com.jjs.ClothingInventorySaleReformPlatform.dto.auth.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.SellerInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
