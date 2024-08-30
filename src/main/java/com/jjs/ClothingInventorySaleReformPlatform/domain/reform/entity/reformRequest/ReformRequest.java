package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request.ReformRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.entity.BaseEntity;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "REFORM_REQUEST")

public class ReformRequest extends BaseEntity {  // 의뢰서 - 의뢰번호, 의뢰자이메일, 디자이너이메일, 의뢰정보, 의뢰사진, 상태, 상품번호


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_NUMBER", nullable = false)
    private Long id;  // 의뢰 번호

    @NotNull
    @Column(name = "REQUEST_INFO", nullable = false, length = 1000)
    private String requestInfo;  // 의뢰 정보

    @NotNull
    @Column(name = "REQUEST_PART", nullable = false)
    private String requestPart;  // 리폼 부위

    @NotNull
    @Column(name = "REQUEST_PRICE", nullable = false)
    private String requestPrice; // 희망 가격

    @Enumerated(EnumType.STRING)
    private ReformRequestStatus requestStatus;

    @OneToMany(mappedBy = "reformRequest", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY) // 리폼 의뢰 첨부 이미지 연결
    private List<ReformRequestImage> reformRequestImageList = new ArrayList<>();

    //
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PURCHASER_EMAIL", nullable = false)
    private PurchaserInfo purchaserEmail;  // 의뢰자(고객) 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DESIGNER_EMAIL", nullable = false)
    private DesignerInfo designerEmail;  // 디자이너 이메일
    //

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_NUMBER", nullable = false)
    private Product productNumber;  // 상품 번호

    /**
     * 요청서 값 수정
     */
    public void updateReformRequestInfo(String requestInfo, String requestPart, String requestPrice) {
        this.requestInfo = requestInfo;
        this.requestPart = requestPart;
        this.requestPrice = requestPrice;
        }

        // 서비스 레이어에서 객체 생성, 값 세팅까지 하는거보다는 이렇게 DTO를 생성하는 메소드로 바꾸는게 좋은지?
    public ReformRequestDTO toDTO() {
        ReformRequestDTO reformRequestDTO = new ReformRequestDTO();
        reformRequestDTO.setRequestInfo(this.getRequestInfo());
        reformRequestDTO.setRequestPart(this.getRequestPart());
        reformRequestDTO.setRequestPrice(this.getRequestPrice());
        reformRequestDTO.setDesignerEmail(this.getDesignerEmail().getEmail());
//        reformRequestDTO.setRequestImg(this.getReformRequestImageList());
        return reformRequestDTO;
    }

}