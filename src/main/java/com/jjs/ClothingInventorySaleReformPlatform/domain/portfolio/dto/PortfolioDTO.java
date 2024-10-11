package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 포트폴리오를 Form-data로 받아서 저장하기 위한 DTO
 * 디자이너 이름을 프론트에서 폼으로 입력하지 않고 받아올 수 있는지 확인 필요함!!
 */
@Getter
@Setter
public class PortfolioDTO {

    @Schema(description = "포트폴리오 ID")
    private Long ID;

    @NotBlank(message = "해당란은 필수 입력 값입니다.")
    @Schema(description = "자기소개 및 경력 설명")
    private String explanation; // 자기소개 & 설명

    @NotNull(message = " 디자이너 이미지는 필수 입력 값입니다.")
    @Schema(description = "디자이너 이미지")
    private MultipartFile designerImage; // 디자이너 이미지

    @NotNull(message = " 디자이너 이름은 필수 입력 값입니다.")
    @Schema(description = "디자이너 이름")
    private String designerName;

//    @NotBlank(message = " 디자이너 이메일은 필수 입력 값입니다.")
    @Schema(description = "디자이너 이메일")
    private String designerEmail; // 디자이너 이메일

    @Schema(description = "가격표 이미지")
    private MultipartFile priceImage; // 가격표 이미지


    public Portfolio toEntity(String userEmail,String storedDesignerImageUrl,String storedPriceImageUrl) { // DTO 값으로 Entity 세팅
        Portfolio portfolio = new Portfolio();
        User user = new User(userEmail);

        // DTO 객체에서 값을 뽑아와서 Entity에 set하는 로직이기에 this 사용
        portfolio.setId(this.getID());
        portfolio.setExplanation(this.getExplanation());
        portfolio.setName(this.getDesignerName());
        portfolio.setDesignerEmail(user);
        portfolio.setDesignerImage(storedDesignerImageUrl);
        portfolio.setReformPrice(storedPriceImageUrl);

        return portfolio;
    }

}
