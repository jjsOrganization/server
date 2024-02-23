package com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "REFORM_REQUEST")
public class ReformRequest {  // 의뢰서 - 의뢰번호, 의뢰자이메일, 디자이너이메일, 의뢰정보, 의뢰사진, 상태, 상품번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_NUMBER", nullable = false)
    private Long id;  // 의뢰 번호

    @NotNull
    @Lob
    @Column(name = "REQUEST_INFO", nullable = false)
    private String requestInfo;  // 의뢰 정보

    @NotNull
    @Column(name = "REQUEST_PART", nullable = false)
    private String requestPart;  // 리폼 부위

    @NotNull
    @Column(name = "REQUEST_PRICE", nullable = false)
    private String requestPrice; // 희망 가격


    @OneToMany(mappedBy = "reformRequest", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY) // 리폼 의뢰 첨부 이미지 연결
    private List<ReformRequestImage> reformRequestImageList = new ArrayList<>();

    //
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PURCHASER_EMAIL", nullable = false)
    private PurchaserInfo clientEmail;  // 의뢰자(고객) 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DESIGNER_EMAIL", nullable = false)
    private DesignerInfo designerEmail;  // 디자이너 이메일
    //

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_NUMBER", nullable = false)
    private Product productNumber;  // 상품 번호
/*

    @OneToMany(mappedBy = "requestNumber")
    private Set<Estimate> estimates = new LinkedHashSet<>();
*/
}