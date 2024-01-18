package com.jjs.ClothingInventorySaleReformPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String email;

    private String password;
    private String name;
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
