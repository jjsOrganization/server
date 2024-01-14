package com.jjs.ClothingInventorySaleReformPlatform.dto;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Designer;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesignerDTO {
    @NotEmpty // 유효성 검사를 위해서 어노테이션 추가 (DTO에서 해야 함)
    private String email;

    private String password;
    private String designerName;
    private String name;
    private String businessNumber;
    private String address;
    private String phoneNumber;


    public static DesignerDTO toDesignerDTO(Designer designer) {
        DesignerDTO designerDTO = new DesignerDTO();

        designerDTO.setEmail(designer.getEmail());
        designerDTO.setPassword(designer.getPassword());
        designerDTO.setDesignerName(designer.getDesignerName());
        designerDTO.setName(designer.getName());
        designerDTO.setBusinessNumber(designer.getBusinessNumber());
        designerDTO.setAddress(designer.getAddress());
        designerDTO.setPhoneNumber(designer.getPhoneNumber());

        return designerDTO;
    }
}
