package com.jjs.ClothingInventorySaleReformPlatform.dto.auth.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DesignerInfoResponse {

    private String email;
    private String name;
    private String phoneNumber;
    private String address;

    public DesignerInfoResponse(String email, String name, String phoneNumber, String address) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public static DesignerInfoResponse from(DesignerInfo designerInfo) {
        return new DesignerInfoResponse(
                designerInfo.getUser().getEmail(),
                designerInfo.getUser().getName(),
                designerInfo.getUser().getPhoneNumber(),
                designerInfo.getAddress()
        );
    }
}
