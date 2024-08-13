package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequestStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReformRequestDTO {

    @NotEmpty
    private String requestPart; // 리폼 의뢰 부위

    @NotEmpty
    private String requestInfo;  // 의뢰 정보(내용)

    private List<MultipartFile> requestImg;  // 의뢰 사진

    private String requestPrice; // 희망 가격

    private String designerEmail; // 요청할 디자이너 이메일

    public ReformRequest toEntity(PurchaserInfo purchaserInfo, DesignerInfo designerInfo, Product product) { // entity로 변경하면서 DTO 값으로 엔티티 값 설정
        ReformRequest reformRequest = new ReformRequest();
        reformRequest.setRequestInfo(this.getRequestInfo());
        reformRequest.setRequestPart(this.getRequestPart());
        reformRequest.setRequestPrice(this.getRequestPrice());
        reformRequest.setRequestStatus(ReformRequestStatus.REQUEST_WAITING);
        reformRequest.setPurchaserEmail(purchaserInfo);
        reformRequest.setDesignerEmail(designerInfo);
        reformRequest.setProductNumber(product);

        return reformRequest;
    }

}
