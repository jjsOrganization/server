package com.jjs.ClothingInventorySaleReformPlatform.dto;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Purchaser;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class PurchaserDTO {

    private String email;

    private String password;
    private String nickname;
    private String name;
    private String address;
    private String phoneNumber;

    /*

    public static PurchaserDTO toPurchaserDTO(Purchaser purchaser) {
        PurchaserDTO purchaserDTO = new PurchaserDTO();
        purchaserDTO.setEmail(purchaser.getEmail());
        purchaserDTO.setPassword(purchaser.getPassword());
        purchaserDTO.setNickname(purchaser.getNickname());
        purchaserDTO.setName(purchaser.getName());
        purchaserDTO.setAddress(purchaser.getAddress());
        purchaserDTO.setPhoneNumber(purchaser.getPhoneNumber());

        return purchaserDTO;
    }

     */

}
